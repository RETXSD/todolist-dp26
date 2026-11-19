# TaskFlow — Todo List dengan Design Pattern

TaskFlow adalah aplikasi web todo list berbasis Java untuk mengelola tugas harian. Project ini dibuat sebagai project pembelajaran Design Pattern dengan Spring Boot, halaman server-rendered Thymeleaf, dan database MySQL.

## Fitur

- Registrasi dan login menggunakan username atau email.
- Buat, edit, hapus, dan tandai tugas sebagai selesai.
- Atur tanggal, waktu, kategori (`PERSONAL`, `WORK`, `SHOPPING`), tingkat urgensi (`URGENT`, `PRIORITY`, `OPTIONAL`), dan pola pengulangan.
- Lihat tugas berdasarkan tanggal, lalu filter atau urutkan berdasarkan kategori, urgensi, atau waktu dibuat.
- Lihat pengingat tugas urgent dan jumlah tugas yang terlambat.
- Batalkan aksi tugas terakhir dengan fitur undo.

## Teknologi

| Bagian | Teknologi |
| --- | --- |
| Bahasa | Java 21 |
| Framework aplikasi web | Spring Boot 4.1.0, Spring MVC |
| Tampilan | Thymeleaf, HTML, CSS |
| Login dan akses | Spring Security, form login berbasis session |
| Database | MySQL, MySQL Connector/J |
| Akses database | JDBC langsung (`DriverManager`, `PreparedStatement`) |
| Build | Maven Wrapper |

Project ini tidak menggunakan Spring Data JPA atau Lombok. Query database dan pemetaan hasil query ditulis di repository menggunakan JDBC; `Task` juga memiliki implementasi Builder manual.

## Design Pattern yang digunakan

| Pattern | Implementasi | Penggunaan |
| --- | --- | --- |
| Singleton | [`AppConfig.ConfigHolder`](src/main/java/com/todolist/config/AppConfig.java) | Menyediakan satu instance konfigurasi aplikasi, default urgensi, dan prioritas urgensi. Menggunakan double-checked locking. |
| Factory | [`TaskFactory`](src/main/java/com/todolist/pattern/creational/TaskFactory.java) | Membuat task, memvalidasi kategori/urgensi, serta memilih nilai tanggal dan urgensi default. |
| Builder | [`Task.TaskBuilder`](src/main/java/com/todolist/model/Task.java) | Menyusun object `Task` dengan beberapa properti opsional. Builder ini ditulis manual. |
| Decorator | [`TaskComponent`](src/main/java/com/todolist/pattern/structural/TaskComponent.java), `BaseTask`, `UrgentTaskDecorator`, `RecurringTaskDecorator` | Menambahkan label urgent dan informasi pengulangan ke tampilan task tanpa mengubah object task dasarnya. |
| Strategy | [`SortStrategy`](src/main/java/com/todolist/pattern/behavioral/strategy/SortStrategy.java), strategi notifikasi | Mengganti algoritma pengurutan task (urgensi, kategori, tanggal dibuat) dan membangun pengingat melalui strategi notifikasi. |
| Observer | [`TaskEventPublisher`](src/main/java/com/todolist/pattern/behavioral/observer/TaskEventPublisher.java), `TaskObserver`, `TaskNotificationListener` | Memberi tahu observer ketika task dibuat, diedit, diselesaikan, atau dihapus. |
| Facade | [`TaskFacade`](src/main/java/com/todolist/facade/TaskFacade.java) | Menyediakan satu pintu operasi task bagi controller dan mengoordinasikan repository, factory, command, event, dan strategi sorting. |
| Command | [`TaskCommand`](src/main/java/com/todolist/pattern/behavioral/command/TaskCommand.java), `CommandHistory` | Membungkus operasi create/edit/complete/delete sebagai command dan menyimpan riwayat per session untuk undo aksi terakhir. |
| State | [`TaskStateResolver`](src/main/java/com/todolist/pattern/behavioral/state/TaskStateResolver.java) dan implementasi `TaskState` | Menentukan status tampilan task: `Pending`, `Overdue`, atau `Completed`. |

## Struktur project

```text
.
├── src/
│   ├── main/
│   │   ├── java/com/todolist/
│   │   │   ├── config/      # Konfigurasi aplikasi dan Spring Security
│   │   │   ├── controller/  # Controller task, login, dan notifikasi
│   │   │   ├── dto/         # Object request/response untuk tampilan
│   │   │   ├── facade/      # TaskFacade
│   │   │   ├── model/       # User, Task, Category, Urgency
│   │   │   ├── pattern/     # Implementasi design pattern
│   │   │   ├── repository/  # Query MySQL menggunakan JDBC
│   │   │   └── service/     # Layanan user, pengingat, log, notifikasi
│   │   └── resources/
│   │       ├── static/      # CSS
│   │       ├── templates/   # Halaman Thymeleaf
│   │       └── application.yaml
│   └── test/                # Test aplikasi
├── todolist.sql             # Schema dan data contoh MySQL
└── pom.xml                  # Dependencies dan konfigurasi Maven
```

## Persyaratan

- JDK 21
- MySQL Server yang bisa diakses di `127.0.0.1:3307` (atau sesuaikan konfigurasi)
- MySQL command-line client atau MySQL Workbench untuk menjalankan file SQL

Maven Wrapper sudah tersedia (`mvnw` dan `mvnw.cmd`), jadi instalasi Maven terpisah tidak diperlukan.

## Menyiapkan database

1. Pastikan MySQL berjalan. Konfigurasi default project memakai port `3307`, username `root`, dan password kosong.
2. Jalankan script [`todolist.sql`](todolist.sql) untuk membuat database `todolist_db`, tabel `users` dan `tasks`, serta data contoh.

Di MySQL client, sambungkan ke server lalu jalankan script dari folder project:

```sh
mysql --host=127.0.0.1 --port=3307 --user=root --password
```

Setelah prompt MySQL muncul, jalankan:

```sql
SOURCE todolist.sql;
```

Perintah koneksi akan meminta password MySQL; untuk password kosong, tekan Enter. Kalau MySQL-mu memakai port, username, atau password berbeda, gunakan nilainya saat koneksi dan ubah juga di [application.yaml](src/main/resources/application.yaml):

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3307/todolist_db
    username: root
    password:
```

Alternatifnya, buka `todolist.sql` di MySQL Workbench yang tersambung ke server tersebut, lalu jalankan script-nya.

## Menjalankan aplikasi

Dari folder project, jalankan:

**Windows PowerShell:**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```sh
./mvnw spring-boot:run
```

Setelah aplikasi mulai, buka [http://localhost:8080](http://localhost:8080). Halaman akan mengarahkan ke login atau daftar tugas.

## Penggunaan

- Daftar akun di `/register`, lalu login di `/login`.
- Halaman utama tugas ada di `/tasks`. Tanggal dapat dipilih; filter kategori, urgensi, dan pengurutan tersedia di halaman tersebut.
- Halaman `/notifications` menampilkan pengingat tugas urgent.
- Untuk menguji data contoh dari `todolist.sql`, login dengan username `demo` dan password `demo123`.

Project ini menggunakan halaman web MVC, bukan API JWT. Route utamanya adalah `/register`, `/login`, `/tasks`, dan `/notifications`; operasi task dilakukan melalui form di halaman web.

## Catatan keamanan

Project ini ditujukan untuk pembelajaran lokal. Implementasi `PasswordEncoder` saat ini membandingkan dan menyimpan password sebagai teks biasa. Jangan gunakan password pribadi atau deploy aplikasi ini ke lingkungan publik sebelum menggantinya dengan password hashing yang aman, misalnya BCrypt.
