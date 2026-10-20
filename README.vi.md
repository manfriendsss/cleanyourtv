<p align="center">
  <a href="https://cleanyour.tv"><img src="docs/banner.png" alt="CleanYourTV: Biến TV thông minh trở nên sạch sẽ, không quảng cáo" width="800"></a>
</p>

<p align="center">
  <a href="README.md">English</a> · <a href="README.zh-CN.md">简体中文</a> · Tiếng Việt
</p>

<p align="center">
  <a href="https://cleanyour.tv">Trang chủ dự án</a> ·
  <a href="PROMPT.md">Lời nhắc AI (Prompt)</a> ·
  <a href="examples/README.md">Danh sách 107 dòng TV</a> ·
  <a href="SHOWCASE.md">Sản phẩm cộng đồng</a>
</p>

# Hướng dẫn sử dụng CleanYourTV (Tiếng Việt)

Dọn sạch rác, quảng cáo và dịch vụ theo dõi (telemetry) trên bất kỳ **Android TV** hoặc **Google TV** nào qua mạng Wi-Fi/Ethernet nội bộ, thay thế màn hình chính đầy quảng cáo bằng một giao diện launcher bản địa siêu nhẹ (~390 KB) mượt mà như Apple TV.

**Không cần root, không cần dây cáp, không gỡ bỏ ứng dụng hệ thống vĩnh viễn (chỉ tắt bằng `pm disable-user`), và có script khôi phục nguyên trạng 100% chỉ bằng 1 dòng lệnh bất cứ khi nào bạn muốn.**

---

![So sánh màn hình gốc Google TV và CleanYourTV](docs/stock-vs-new-side.jpg)

## Nguyên lý hoạt động

TV thông minh chạy Android TV / Google TV dùng chung cầu nối gỡ lỗi (ADB) giống điện thoại Android. Sau khi bật USB Debugging trong Cài đặt nhà phát triển trên TV và cấp quyền kết nối từ máy tính qua mạng nội bộ:

1. **Sao lưu**: Tạo bản sao lưu danh sách ứng dụng đã cài và các cài đặt liên quan.
2. **Vô hiệu hóa (Debloat)**: Tắt các dịch vụ quảng cáo, gợi ý nội dung tài trợ và ứng dụng rác cài sẵn của hãng bằng lệnh `pm disable-user`. Vì chỉ vô hiệu hóa chứ không gỡ bỏ hoàn toàn, bạn luôn có thể khôi phục chỉ với một lệnh.
3. **Cài đặt launcher tối giản**: Cài đặt ứng dụng CleanYourTV launcher và đặt làm màn hình chính mặc định.

Mỗi hãng TV (Sony, Xiaomi, TCL, Casper, Coocaa, Chromecast...) có bộ ứng dụng rác khác nhau. Bản hướng dẫn AI ([`PROMPT.md`](PROMPT.md)) sẽ hướng dẫn trợ lý AI kiểm tra thực tế TV của bạn và hỏi bạn trước khi thao tác.

---

## Bắt đầu nhanh (Quick Start)

Bạn cần một máy tính cùng mạng Wi-Fi với TV và một trợ lý AI lập trình (như Antigravity, Claude Code, Codex, hoặc Gemini CLI).

| Bước | Thao tác |
|---|---|
| **1. Trên TV** | Vào **Cài đặt** → **Tùy chọn thiết bị** → **Giới thiệu** → bấm phím OK liên tục 7 lần vào mục **Bản dựng (Build number)** để bật Chế độ nhà phát triển. Quay lại, vào **Tùy chọn cho nhà phát triển**, bật **Gỡ lỗi USB (USB debugging)** (nếu có thêm mục Gỡ lỗi mạng / Wireless debugging thì bật cả hai). Giữ TV luôn sáng màn hình. |
| **2. Trên máy tính** | Sao chép nội dung file [`PROMPT.md`](PROMPT.md) đưa vào trợ lý AI và điền mã model TV của bạn. |
| **3. Cấp quyền & Xác nhận** | Màn hình TV sẽ hiện popup *"Cho phép gỡ lỗi USB?"* → chọn **Luôn cho phép**. Trả lời danh sách câu hỏi của AI: ứng dụng bạn hay dùng, 4 app muốn để ở thanh dock dưới cùng, và có muốn giữ trợ lý giọng nói (Google Assistant), Chromecast, cập nhật hệ thống hay không. |
| **4. Kiểm tra kết quả** | Trợ lý AI sẽ tự động sao lưu, dọn rác, build và cài đặt launcher, sau đó chụp màn hình TV để bạn xác nhận. Toàn bộ quá trình chỉ mất khoảng 10-15 phút. |

---

## Các tính năng của Launcher CleanYourTV

- **Màn hình chính đen tuyền (OLED black)**: Thanh dock bo cong ở cạnh dưới chứa 4 ứng dụng bạn yêu thích nhất (như phong cách tvOS của Apple TV).
- **Xem tiêu điểm (Top Shelf)**: Tên ứng dụng đang chọn hiển thị chữ lớn, hiệu ứng ánh sáng hào quang (ambient glow) tự động trích xuất theo dải màu biểu tượng/banner của ứng dụng.
- **Lưới ứng dụng đầy đủ**: Bấm phím Xuống để trượt lên danh sách toàn bộ ứng dụng còn lại, hỗ trợ tùy chỉnh 4, 5, 6 hoặc 7 cột mỗi hàng.
- **Sắp xếp thứ tự bằng remote**: Giữ phím OK trên nút Cài đặt để vào chế độ sắp xếp ứng dụng theo ý thích.
- **Thư viện ứng dụng (App Library)**: Duyệt mọi ứng dụng đã cài và thêm/bớt phím tắt ra màn hình chính bằng remote.
- **3 nút chức năng kính mờ tiện lợi**:
  - **Nguồn vào (Inputs)**: Liệt kê các cổng HDMI (theo tên bạn đã đặt trong cài đặt TV), AV, Truyền hình kỹ thuật số.
  - **Mạng (Network)**: Hiển thị trạng thái Wi-Fi/Ethernet kèm tên mạng đang kết nối.
  - **Cài đặt (Settings)**: Bấm nhanh để mở Cài đặt TV; giữ phím OK để mở menu Cài đặt Launcher.
- **Đồng hồ & Ngày tháng**: Phông chữ Outfit thanh lịch, kèm màn hình chờ (Screensaver) đồng hồ tối giản.
- **Thống kê phần cứng (Stats)**: Tùy chọn bật hiển thị một dòng thông tin dưới đồng hồ: `% CPU`, `RAM trống (MB)`, `Bộ nhớ trống (GB/MB)`.
- **Hệ thống Theme phong phú**: Thay đổi màu sắc, hình nền, thanh dock và cột ứng dụng tức thì qua file `config.json` mà không cần biên dịch lại APK.
- **Thẻ mô-đun (Module Cards)**: Hỗ trợ các ứng dụng khác tích hợp hiển thị thẻ trên màn hình chính (khi được bạn cấp quyền).

![Hiệu ứng tiêu điểm ứng dụng](docs/focus-2-up-netflix.png)

---

## Lịch tự động đổi Theme Sự Kiện & Sinh Nhật Gia Đình

Launcher tích hợp sẵn bộ lên lịch tự động thông minh (**EventScheduler**). Bạn không cần chạy lệnh thủ công; đến đúng ngày, launcher sẽ tự động đổi lời chúc, hiệu ứng ánh sáng hào quang, màu sắc biểu tượng và tự động trở về giao diện mặc định khi hết thời gian:

### 1. Sinh nhật Gia Đình (Chạy trước 1 ngày - Kết thúc sau 1 ngày, ưu tiên cao nhất)
* **Sinh nhật Momo (17/01)**: Tự động chạy từ **16/01 đến 18/01** (Tone vàng cam ấm áp, lời chúc sinh nhật Momo kèm bánh kem 🎂🎉).
* **Sinh nhật Bé Mía (08/03)**: Tự động chạy từ **07/03 đến 09/03** (Tone hồng đào ngọt ngào, lời chúc Bé Mía 🎂🌸, tự động ưu tiên hơn ngày 8/3 trong 3 ngày này).
* **Sinh nhật Bố Quang Anh (28/06)**: Tự động chạy từ **27/06 đến 29/06** (Tone xanh dương hoàng gia lịch lãm 🎂🎈).
* **Sinh nhật Mẹ Linh (20/11)**: Tự động chạy từ **19/11 đến 21/11** (Tone tím hoa sen quý phái 🎂💐).

### 2. Các Ngày Lễ Lớn (Chạy trước 2 tuần - Kết thúc sau 1 tuần)
* **Tết Nguyên Đán**: Chạy từ **2 tuần trước Mùng 1 Tết (15 tháng Chạp) đến hết Mùng 7 Tết** (Tone đỏ may mắn, vàng hoàng kim, lời chúc Tết bình an vạn sự như ý 🧧✨, tự động tính theo lịch âm 2024–2040).
* **Giáng Sinh & Năm Mới (25/12)**: Chạy từ **11/12 đến hết 01/01** năm mới (Tone đỏ noel & xanh thông tuyết 🎄✨).
* **Lễ Tình Nhân Valentine (14/02)**: Chạy từ **31/01 đến hết 21/02** (Tone đỏ hồng ngọt ngào 💖).
* **Quốc Tế Phụ Nữ (08/03)**: Chạy từ **22/02 đến hết 15/03** (Tone tím hồng rạng rỡ 💐).
* **Tự Hào Việt Nam (30/04 - 01/05)**: Chạy từ **16/04 đến hết 08/05** (Tone đỏ cờ Tổ quốc 🇻🇳).
* **Quốc Khánh Nước CHXHCN Việt Nam (02/09)**: Chạy từ **19/08 đến hết 09/09** (Tone đỏ cờ Tổ quốc 🇻🇳).

> Khi hết khoảng thời gian sự kiện, giao diện sẽ tự động chuyển về theme bạn đã chọn ban đầu mà không cần bất kỳ thao tác nào. Bạn cũng có thể chủ động nạp nhanh từng theme bằng lệnh:
> `powershell -ExecutionPolicy Bypass -File tools\theme.ps1 -TvHost <IP_TV> themes\<ten-theme>`

---

## Cài đặt trực tiếp trên TV

- **Bấm nhanh phím OK** vào nút **Cài đặt**: Mở Cài đặt hệ thống của TV.
- **Nhấn giữ phím OK** vào nút **Cài đặt**: Mở menu tùy chọn CleanYourTV:
  - **Cài đặt launcher**:
    - *Số ứng dụng mỗi hàng*: 4, 5, 6 hoặc 7 cột.
    - *Hiệu ứng (Animations)*: Bật hoặc tắt hiệu ứng chuyển động.
    - *Hiệu ứng nâng cao (Advanced effects)*: Hiệu ứng ô nổi lên, phóng to biểu tượng, chuyển màu mượt.
    - *Theo theme (Follow theme)*: Đặt lại về giao diện theme mặc định.
  - **Chọn màn hình chính mặc định**: Chuyển đổi qua lại giữa CleanYourTV và giao diện gốc của TV.
  - **Sắp xếp ứng dụng**: Di chuyển vị trí các ô ứng dụng trên màn hình.
  - **Thẻ mô-đun**: Quản lý các thẻ widget tiện ích từ các ứng dụng khác.

---

## Thao tác thủ công qua dòng lệnh

Dành cho người dùng muốn tự chạy lệnh thay vì dùng trợ lý AI. Thay thế `192.168.1.50` bằng địa chỉ IP TV của bạn.

### Trên Windows (PowerShell)

```powershell
# 1. Cài đặt thư viện Python kết nối TV
python -m venv tools\venv
tools\venv\Scripts\python.exe -m pip install -r tools\requirements.txt

# 2. Kiểm tra thông tin TV
$env:TV_HOST = "192.168.1.50"
tools\venv\Scripts\python.exe -I tools\tv.py --probe

# 3. Chạy script vô hiệu hóa ứng dụng rác (thay bằng file model phù hợp)
powershell -ExecutionPolicy Bypass -File tools\disable.ps1 examples\xiaomi-mi-tv-4a\debloat.txt

# 4. Biên dịch và cài đặt launcher
powershell -ExecutionPolicy Bypass -File launcher\build-and-install.ps1 -TvHost 192.168.1.50

# 5. Đổi giao diện theme
powershell -ExecutionPolicy Bypass -File tools\theme.ps1 -TvHost 192.168.1.50 themes\aurora
```

### Trên macOS / Linux (Bash)

```bash
# 1. Chuẩn bị môi trường
python3 -m venv tools/venv
tools/venv/bin/pip install -r tools/requirements.txt
export TV_HOST=192.168.1.50

# 2. Kiểm tra TV
tools/venv/bin/python -I tools/tv.py --probe

# 3. Dọn dẹp ứng dụng rác
tools/disable.sh examples/xiaomi-mi-tv-4a/debloat.txt

# 4. Cài đặt launcher
launcher/build-and-install.sh

# 5. Áp dụng theme
tools/theme.sh themes/aurora
```

---

## Khôi phục về trạng thái ban đầu (Restore)

Nếu bạn muốn trở lại giao diện gốc của hãng hoặc mang TV đi bảo hành:

```powershell
# Trên Windows
powershell -ExecutionPolicy Bypass -File tools\restore.ps1

# Trên macOS / Linux
tools/restore.sh
```

Lệnh này sẽ bật lại tất cả ứng dụng hệ thống đã bị tắt và trả TV về nguyên vẹn như lúc xuất xưởng.

---

## Bản quyền & Đóng góp

- Giấy phép: MIT License.
- Được tạo bởi [Gathin23](https://github.com/Gathin23/cleanyourtv).
- Bản fork và bản địa hóa tiếng Việt: [manfriendsss/cleanyourtv](https://github.com/manfriendsss/cleanyourtv).
