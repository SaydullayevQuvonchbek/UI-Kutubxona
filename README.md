# Kutubxona mobil ilovasi (Android Native, Kotlin)

Mohirdev Android dasturlash kursi bo'yicha 2-amaliy vazifa: Kutubxona loyihasi.

## Loyiha haqida
Ushbu mobil ilova orqali kutubxonadagi kitoblarni va foydalanuvchilar ijarasini nazorat qilish mumkin. Dasturda ikki turdagi rol mavjud:
- **Admin**: Kutubxonaga yangi kitoblar qo'sha oladi, foydalanuvchilar ro'yxatini hamda kim qanday kitob olganligini, olingan sana va qaytarish muddatlarini ko'ra oladi.
- **Foydalanuvchi**: Kutubxona bazasidan o'ziga kerakli kitoblarni oladi hamda o'zida turgan kitoblar muddatini kuzatib, ularni qayta topshira oladi.

## Texnologiyalar
- Dasturlash tili: Kotlin
- UI arxitekturasi: XML Layout, Material Components, ViewBinding
- Ma'lumotlarni saqlash: SharedPreferences (Gson yordamida JSON formati)

## Dasturni o'rnatish va tekshirish

### 1. APK faylni o'rnatish
Loyiha papkasida tayyor `Kutubxona.apk` fayli mavjud. Uni Android telefoningizga o'tkazib o'rnatishingiz va tizimni tekshirishingiz mumkin.

### 2. Android Studio orqali ochish
1. Loyihani Android Studio dasturida oching.
2. Gradle fayllarini sinxronizatsiya qiling.
3. Emulator yoki real qurilmada ishga tushiring.

## Demo hisoblar
Tizimni tezroq sinab ko'rish uchun quyidagi tayyor hisoblardan foydalansangiz bo'ladi:
- Admin uchun — login: `admin`, parol: `admin`
- Foydalanuvchi uchun — login: `user`, parol: `user`
