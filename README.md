# 🍏 FoodSaver

<div align="center">

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.sqlite.org/)
[![License](https://img.shields.io/badge/Lisans-MIT-blue.svg?style=for-the-badge)](LICENSE)

**Evdeki gıda envanterini takip etmek, son kullanma tarihi yaklaşan ürünleri hatırlatmak ve mevcut malzemelere göre yemek tarifleri önermek için geliştirilmiş Android mobil uygulaması.**

[Özellikler](#-öne-çıkan-özellikler) • [Ekran Görüntüleri](#-ekran-görüntüleri) • [Teknolojiler](#-kullanılan-teknolojiler) • [Kurulum](#-kurulum-ve-çalıştırma) 
</div>

---

## 📱 Proje Hakkında

**FoodSaver**, evdeki gıda israfını önlemek, mutfak harcamalarını kontrol altında tutmak ve gıda tüketimini planlı hale getirmek amacıyla geliştirilmiş kapsamlı bir Android uygulamasıdır.

Kullanıcılar evlerindeki gıda ürünlerini adı, miktarı ve son kullanma tarihi bilgileriyle sisteme kaydedebilir. Uygulama, son kullanma tarihi yaklaşan ürünler için kullanıcıya hatırlatmalar sunarak gıdaların bozulmadan zamanında tüketilmesini sağlar. Bununla birlikte; gıdaların raf ömrünü uzatmaya yönelik doğru saklama ipuçları, mevsimsel beslenme önerileri ve mutfakta halihazırda bulunan malzemelerle hazırlanabilecek pratik yemek tarifleri önererek mutfak yönetimini kolaylaştırır.

---

## 📸 Ekran Görüntüleri

<div align="center">
  <table>
    <tr>
      <td align="center"><b>Ana Ekran & Envanter</b></td>
      <td align="center"><b>Ürün Ekleme</b></td>
      <td align="center"><b>Tarif Önerileri</b></td>
      <td align="center"><b>Saklama İpuçları</b></td>
    </tr>
    <tr>
      <td><img src="screenshots/home.png" width="200" alt="Ana Ekran"/></td>
      <td><img src="screenshots/add_item.png" width="200" alt="Ürün Ekleme"/></td>
      <td><img src="screenshots/recipes.png" width="200" alt="Tarif Önerileri"/></td>
      <td><img src="screenshots/tips.png" width="200" alt="Saklama İpuçları"/></td>
    </tr>
  </table>
</div>

---

## ✨ Öne Çıkan Özellikler

* **📦 Gıda Envanter Yönetimi:** Evdeki ürünleri adı, miktarı (adet, kg, litre vb.) ve son kullanma tarihiyle hızlıca ekleme, düzenleme ve listeleme.
* **⏰ Son Kullanma Tarihi Takibi ve Hatırlatmalar:** Tarihi yaklaşan veya geçmek üzere olan gıdalar için görsel uyarılar ve hatırlatmalar sunarak israfı önleme.
* **🍳 Akıllı Tarif Önerileri:** Mutfakta mevcut olan malzemeleri analiz ederek eldeki ürünlerle yapılabilecek uygun yemek tariflerini listeleme.
* **❄️ Saklama İpuçları ve Raf Ömrü Bilgileri:** Sebze, meyve, bakliyat ve süt ürünleri gibi farklı gıda gruplarının nasıl daha uzun süre taze tutulacağına dair pratik bilgiler.
* **🌱 Mevsimsel Öneriler:** Yılın bulunduğunuz ayına ve mevsimine göre en taze tüketilebilecek gıda önerileri ve bilgilendirmeler.
* **⚡ Çevrimdışı Çalışma Desteği:** SQLite yerel veritabanı mimarisi sayesinde internet bağlantısına ihtiyaç duymadan tüm verilere anında erişim.

---

## 🛠️ Kullanılan Teknolojiler

| Kategori | Teknoloji / Araç | Açıklama |
| :--- | :--- | :--- |
| **Programlama Dili** | Java | Uygulamanın iş mantığı ve arka plan işlemleri |
| **Kullanıcı Arayüzü (UI)** | XML | Android Native bileşenler ve duyarlı ekran tasarımları |
| **Veritabanı** | SQLite | Ürün, tarih ve tarif verilerinin yerel cihazda saklanması |
| **Geliştirme Ortamı (IDE)** | Android Studio | Kod geliştirme, derleme ve emülatör test süreçleri |

---

## 📂 Proje Klasör Yapısı

```text
FoodSaver/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/foodsaver/   # Java kaynak kodları (Activity, Adapter, DatabaseHelper)
│   │   │   ├── res/
│   │   │   │   ├── layout/                   # XML ekran tasarımları
│   │   │   │   ├── drawable/                 # İkonlar ve görsel varlıklar
│   │   │   │   └── values/                   # Renkler, metinler (strings.xml) ve temalar
│   │   │   └── AndroidManifest.xml           # Uygulama izinleri ve bileşen tanımları
├── screenshots/                              # README ekran görüntüleri
└── README.md                                 # Proje dokümantasyonu

🚀 Kurulum ve Çalıştırma
Projeyi yerel geliştirme ortamınızda çalıştırmak için aşağıdaki adımları takip edebilirsiniz:

Gereksinimler
Bilgisayarınızda Android Studio (tercihen güncel sürüm) kurulu olmalıdır.

Android SDK (API 24 / Android 7.0 ve üzeri) yüklü olmalıdır.

Adım Adım Kurulum
1-Depoyu bilgisayarınıza klonlayın:
git clone [https://github.com/FeyzaDemirkiran/FoodSaver.git](https://github.com/feyzademirkiran/FoodSaver.git)

2-Projeyi Android Studio'da açın:
Android Studio'yu başlatın ve Open seçeneğine tıklayın.
Klonladığınız FoodSaver proje klasörünü seçerek onaylayın.

3-Gradle senkronizasyonunu tamamlayın:
Proje açıldığında sağ altta yer alan Gradle derleme ve kütüphane indirme işleminin bitmesini bekleyin.

4-Uygulamayı çalıştırın:
Üst menüden bir Android Sanal Cihaz (AVD / Emülatör) seçin veya USB hata ayıklama modu açık bir Android telefon bağlayın.
Run 'app' (Shift + F10) butonuna tıklayarak uygulamayı başlatın.

💡 Gelecek Geliştirmeler (Roadmap)
[ ] Tarihi yaklaşan ürünler için arka planda çalışan anlık bildirim (Push Notification) sistemi.

[ ] Ürün ambalajlarındaki barkodları kamera ile okutarak otomatik ürün ekleme.

[ ] Eksilen ürünler için otomatik alışveriş listesi oluşturma.

[ ] Kullanıcı hesabı ile bulut yedekleme (Firebase / Cloud Sync) desteği.
