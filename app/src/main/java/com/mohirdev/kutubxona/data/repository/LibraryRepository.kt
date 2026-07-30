package com.mohirdev.kutubxona.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohirdev.kutubxona.data.model.Book
import com.mohirdev.kutubxona.data.model.BookLoan
import com.mohirdev.kutubxona.data.model.Role
import com.mohirdev.kutubxona.data.model.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class LibraryRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("kutubxona_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val KEY_USERS = "key_users"
    private val KEY_BOOKS = "key_books"
    private val KEY_LOANS = "key_loans"
    private val KEY_CURRENT_USER_ID = "key_current_user_id"

    init {
        initDefaultDataIfEmpty()
    }

    private fun initDefaultDataIfEmpty() {
        val users = getUsers()
        if (users.isEmpty()) {
            val defaultUsers = listOf(
                User(
                    id = "admin_1",
                    fullName = "Sardor Adminov",
                    username = "admin",
                    password = "admin",
                    role = Role.ADMIN,
                    registeredDate = formatDate(Date())
                ),
                User(
                    id = "user_1",
                    fullName = "Azizbek Rahimov",
                    username = "user",
                    password = "user",
                    role = Role.USER,
                    registeredDate = formatDate(Date())
                ),
                User(
                    id = "user_2",
                    fullName = "Jasurbek Tursunov",
                    username = "jasur",
                    password = "12345",
                    role = Role.USER,
                    registeredDate = formatDate(Date())
                )
            )
            saveUsers(defaultUsers)

            val defaultBooks = listOf(
                Book(
                    id = "book_1",
                    title = "O\'tkan kunlar",
                    author = "Abdulla Qodiriy",
                    genre = "Tarixiy roman",
                    totalCopies = 5,
                    availableCopies = 4,
                    description = "O\'zbek adabiyotidagi ilk roman. Otabek va Kumushbibining sof muhabbati haqida."
                ),
                Book(
                    id = "book_2",
                    title = "Mehrobdan chayon",
                    author = "Abdulla Qodiriy",
                    genre = "Tarixiy roman",
                    totalCopies = 3,
                    availableCopies = 3,
                    description = "Anvar va Ra\'noning hayoti hamda Xudoyorxon davri voqealari."
                ),
                Book(
                    id = "book_3",
                    title = "Shaytanat",
                    author = "Tohir Malik",
                    genre = "Detektiv / Asadbek",
                    totalCopies = 4,
                    availableCopies = 3,
                    description = "Jinoyat olami va uning fojiali oqibatlari haqida badiiy asar."
                ),
                Book(
                    id = "book_4",
                    title = "Xamsa",
                    author = "Alisher Navoiy",
                    genre = "Doston / Mumtoz",
                    totalCopies = 2,
                    availableCopies = 2,
                    description = "Buyuk shoir Alisher Navoiyning besh dostondan iborat shoh asari."
                ),
                Book(
                    id = "book_5",
                    title = "Alkimyogar",
                    author = "Paulo Koelyo",
                    genre = "Falsafiy roman",
                    totalCopies = 6,
                    availableCopies = 6,
                    description = "O\'z orzusi ortidan borgan cho\'pon yigit haqida mashhur falsafiy asar."
                )
            )
            saveBooks(defaultBooks)

            // Add demo loan for user_1 ("Azizbek Rahimov")
            val now = System.currentTimeMillis()
            val tenDaysAgo = now - 10L * 24 * 60 * 60 * 1000
            val fiveDaysLater = now + 5L * 24 * 60 * 60 * 1000
            val defaultLoans = listOf(
                BookLoan(
                    id = "loan_1",
                    bookId = "book_1",
                    bookTitle = "O\'tkan kunlar",
                    bookAuthor = "Abdulla Qodiriy",
                    userId = "user_1",
                    userFullName = "Azizbek Rahimov",
                    borrowDate = formatTimestamp(tenDaysAgo),
                    dueDate = formatTimestamp(fiveDaysLater),
                    dueTimestamp = fiveDaysLater,
                    isReturned = false
                ),
                BookLoan(
                    id = "loan_2",
                    bookId = "book_3",
                    bookTitle = "Shaytanat",
                    bookAuthor = "Tohir Malik",
                    userId = "user_2",
                    userFullName = "Jasurbek Tursunov",
                    borrowDate = formatTimestamp(now - 15L * 24 * 60 * 60 * 1000),
                    dueDate = formatTimestamp(now - 1L * 24 * 60 * 60 * 1000),
                    dueTimestamp = now - 1L * 24 * 60 * 60 * 1000, // Overdue demo!
                    isReturned = false
                )
            )
            saveLoans(defaultLoans)
        }
    }

    // AUTHENTICATION
    fun login(username: String, pass: String): User? {
        val users = getUsers()
        val user = users.find { it.username.equals(username, ignoreCase = true) && it.password == pass }
        if (user != null) {
            setCurrentUser(user)
        }
        return user
    }

    fun register(fullName: String, username: String, pass: String, role: Role): Pair<Boolean, String> {
        val users = getUsers().toMutableList()
        if (users.any { it.username.equals(username, ignoreCase = true) }) {
            return Pair(false, "Bu foydalanuvchi nomi avval ro'yxatdan o'tgan!")
        }
        val newUser = User(
            id = UUID.randomUUID().toString(),
            fullName = fullName,
            username = username,
            password = pass,
            role = role,
            registeredDate = formatDate(Date())
        )
        users.add(newUser)
        saveUsers(users)
        setCurrentUser(newUser)
        return Pair(true, "Muvaffaqiyatli ro'yxatdan o'tildi!")
    }

    fun getCurrentUser(): User? {
        val userId = prefs.getString(KEY_CURRENT_USER_ID, null) ?: return null
        return getUserById(userId)
    }

    fun setCurrentUser(user: User?) {
        if (user == null) {
            prefs.edit().remove(KEY_CURRENT_USER_ID).apply()
        } else {
            prefs.edit().putString(KEY_CURRENT_USER_ID, user.id).apply()
        }
    }

    // USERS MANAGEMENT
    fun getUsers(): List<User> {
        val json = prefs.getString(KEY_USERS, null) ?: return emptyList()
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getRegularUsers(): List<User> {
        return getUsers().filter { it.role == Role.USER }
    }

    fun getUserById(userId: String): User? {
        return getUsers().find { it.id == userId }
    }

    private fun saveUsers(users: List<User>) {
        prefs.edit().putString(KEY_USERS, gson.toJson(users)).apply()
    }

    // BOOKS MANAGEMENT
    fun getBooks(): List<Book> {
        val json = prefs.getString(KEY_BOOKS, null) ?: return emptyList()
        val type = object : TypeToken<List<Book>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getBookById(bookId: String): Book? {
        return getBooks().find { it.id == bookId }
    }

    fun addBook(title: String, author: String, genre: String, totalCopies: Int, desc: String): Book {
        val books = getBooks().toMutableList()
        val newBook = Book(
            id = UUID.randomUUID().toString(),
            title = title,
            author = author,
            genre = genre,
            totalCopies = totalCopies,
            availableCopies = totalCopies,
            description = desc
        )
        books.add(newBook)
        saveBooks(books)
        return newBook
    }

    fun updateBook(updatedBook: Book) {
        val books = getBooks().toMutableList()
        val index = books.indexOfFirst { it.id == updatedBook.id }
        if (index != -1) {
            books[index] = updatedBook
            saveBooks(books)
        }
    }

    fun deleteBook(bookId: String) {
        val books = getBooks().toMutableList()
        books.removeAll { it.id == bookId }
        saveBooks(books)
    }

    private fun saveBooks(books: List<Book>) {
        prefs.edit().putString(KEY_BOOKS, gson.toJson(books)).apply()
    }

    // LOANS MANAGEMENT
    fun getAllLoans(): List<BookLoan> {
        val json = prefs.getString(KEY_LOANS, null) ?: return emptyList()
        val type = object : TypeToken<List<BookLoan>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getLoansForUser(userId: String): List<BookLoan> {
        return getAllLoans().filter { it.userId == userId }
    }

    fun borrowBook(bookId: String, userId: String, days: Int): Pair<Boolean, String> {
        val book = getBookById(bookId) ?: return Pair(false, "Kitob topilmadi!")
        if (book.availableCopies <= 0) {
            return Pair(false, "Bu kitobning mavjud nusxalari tugagan!")
        }
        val user = getUserById(userId) ?: return Pair(false, "Foydalanuvchi topilmadi!")

        // Check if user already borrowed this book and hasn't returned it
        val activeLoans = getLoansForUser(userId).filter { !it.isReturned && it.bookId == bookId }
        if (activeLoans.isNotEmpty()) {
            return Pair(false, "Siz bu kitobni avval olgansiz va hali topshirmagansiz!")
        }

        book.availableCopies -= 1
        updateBook(book)

        val now = System.currentTimeMillis()
        val dueTimestamp = now + days * 24L * 60 * 60 * 1000

        val newLoan = BookLoan(
            id = UUID.randomUUID().toString(),
            bookId = book.id,
            bookTitle = book.title,
            bookAuthor = book.author,
            userId = user.id,
            userFullName = user.fullName,
            borrowDate = formatTimestamp(now),
            dueDate = formatTimestamp(dueTimestamp),
            dueTimestamp = dueTimestamp,
            isReturned = false
        )

        val loans = getAllLoans().toMutableList()
        loans.add(0, newLoan) // Add to top
        saveLoans(loans)

        return Pair(true, "Kitob muvaffaqiyatli olindi!")
    }

    fun returnBook(loanId: String): Pair<Boolean, String> {
        val loans = getAllLoans().toMutableList()
        val index = loans.indexOfFirst { it.id == loanId }
        if (index == -1) return Pair(false, "Ijara yozuvi topilmadi!")

        val loan = loans[index]
        if (loan.isReturned) return Pair(false, "Bu kitob allaqachon topshirilgan!")

        loan.isReturned = true
        loan.returnedDate = formatDate(Date())
        loans[index] = loan
        saveLoans(loans)

        val book = getBookById(loan.bookId)
        if (book != null) {
            book.availableCopies = (book.availableCopies + 1).coerceAtMost(book.totalCopies)
            updateBook(book)
        }

        return Pair(true, "Kitob muvaffaqiyatli kutubxonaga topshirildi!")
    }

    private fun saveLoans(loans: List<BookLoan>) {
        prefs.edit().putString(KEY_LOANS, gson.toJson(loans)).apply()
    }

    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(date)
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
