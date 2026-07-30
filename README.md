# Kutubxona — Android Native (Kotlin) Dasturi
**Mohirdev Vazifa 2: Kutubxona**

Ushbu dastur Android Native platformasida **Kotlin** tilida va **Material Components (XML ViewBinding)** texnologiyalari yordamida yaratilgan to'liq funksional kutubxona boshqaruv tizimidir.

---

## 🌟 Asosiy Imkoniyatlar va Vazifa Shartlarining Bajarilishi

### 1. 🔐 Ro'yxatdan o'tish va Kirish (Admin yoki Foydalanuvchi)
* Fovdalanuvchilar o'z ism-familiyasi, logini va paroli bilan ro'yxatdan o'tishi mumkin.
* Ro'yxatdan o'tish jarayonida rolni tanlash imkoniyati mavjud: **Admin** yoki **Foydalanuvchi**.
* Tizimda tayyor demo hisoblar mavjud bo'lib, bir tugma bilan tezkor kirib sinab ko'rish mumkin:
  * **Admin demo hisobi**: login: `admin` | parol: `admin`
  * **Foydalanuvchi demo hisobi**: login: `user` | parol: `user`

### 2. 🛡️ Admin Paneli (3 ta asosiy bo'lim):
* **Kitoblar bo'limi**:
  * Barcha kitoblarni ko'rish, qidirish va qolgan nusxalar sonini nazorat qilish.
  * **Kitob qo'shish**: Yangi kitob nomi, muallifi, janri, jami nusxalari va tavsifini kiritib qo'sha oladi.
  * Kitoblarni o'chirish yoki tahrirlash imkoniyati.
* **Foydalanuvchilar bo'limi**:
  * Tizimdan ro'yxatdan o'tgan barcha foydalanuvchilar ro'yxatini va ularda hozir nechta ijaradagi kitob borligini ko'ra oladi.
  * **Foydalanuvchida bor kitoblar haqida to'liq ma'lumot**: Foydalanuvchi ustiga bosganda undagi barcha kitoblar, **qachon olganligi**, **qachon topshirishi kerakligi** va **holati** (muddati o'tgan yoki o'tmaganligi) batafsil ko'rsatiladi.
* **Barcha ijara kitoblar bo'limi**:
  * Kutubxona bo'yicha berilgan barcha kitoblarni va ularning muddatlarini bir joyda ko'rib boradi.

### 3. 👤 Foydalanuvchi Paneli (2 ta asosiy bo'lim):
* **Kutubxona kitoblari**:
  * Barcha mavjud kitoblarni ko'rish va qidirish.
  * **Kitob olish**: O'ziga yoqqan kitobdan **"Kitob olish"** tugmasini bosib 7, 14 yoki 30 kunga oladi. Bu jarayonda kitobning mavjud nusxalar soni avtomat kamayadi.
* **Mening kitoblarim**:
  * Foydalanuvchining o'zi olgan kitoblari, olingan sana va topshirilishi kerak bo'lgan muddatlar ro'yxati.
  * **Topshirish**: Kitob o'qib bo'lingach, "Topshirish" tugmasi orqali kutubxonaga qaytariladi va nusxa soni tiklanadi.

---

## 🛠️ Texnologiyalar va Arxitektura
* **Til**: Kotlin
* **UI**: XML Layouts + Material Design Components + ViewBinding
* **Ma'lumotlar bazasi**: `SharedPreferences` + `Gson` (Local JSON repository)
* **Arxitektura**: Clean Repository pattern (`LibraryRepository`) + RecyclerView Adapters + ViewPager2

---

## 🚀 Dasturni ishga tushirish va O'rnatish
### 1. APK orqali to'g'ridan-to'g'ri o'rnatish (Eng oson usul)
Loyiha asosiy papkasida **`Kutubxona.apk`** fayli tayyor holatda joylashtirilgan.
1. Ushbu `Kutubxona.apk` faylini Android telefoningizga yuklab oling.
2. Faylni ochib, o'rnatish (Install) tugmasini bosing.
3. Dasturni ochib, yuqoridagi demo hisoblar (`admin`/`admin` yoki `user`/`user`) orqali tizimni sinab ko'ring.

### 2. Android Studio orqali ishga tushirish
1. Loyihani **Android Studio**da oching.
2. Gradle sinxronizatsiyani (Sync Project with Gradle Files) bajaring.
3. Emulator yoki real Android qurilmasida `Run app (Shift + F10)` tugmasini bosing.
