# LAB 7 — ระบบประมวลผลคำสั่งซื้อ กับ Design Patterns

**วิชา 01418211 Software Construction 3(2-2-5)  ·  ปฏิบัติการ 2 ชั่วโมง**

> เนื้อหา: Strategy · Factory · Decorator · Observer (Week 6)
> พื้นฐาน: TestRunner เขียนมือ (Week 3) · record & interface (Week 4–5)

---

## เป้าหมาย

สร้างระบบจำลองการประมวลผลคำสั่งซื้อ (Order) โดยใช้ Design Patterns 4 รูปแบบดูแลคนละส่วน:

| ส่วนของระบบ | Pattern | หน้าที่ |
|---|---|---|
| การคำนวณราคา (ส่วนลด) | **Strategy** | สลับวิธีคิดส่วนลดได้อิสระ |
| การสร้างการจัดส่ง | **Factory** | เลือกชนิดการจัดส่งจาก type |
| บริการเสริม (ห่อของขวัญ/ประกัน) | **Decorator** | ห่อเพิ่มความสามารถทีละชั้น |
| การแจ้งเตือน | **Observer** | คำสั่งซื้อใหม่ → แจ้งหลายฝ่าย |

ภาพรวมการทำงาน: ลูกค้าสร้าง Order → ระบบคิดราคาด้วย **กลยุทธ์ส่วนลด** → สร้าง **การจัดส่ง** ด้วยโรงงาน
→ ลูกค้าเพิ่ม **บริการเสริม** → เมื่อยืนยัน ระบบ **แจ้งเตือน** ส่วนที่เกี่ยวข้อง

---

## แลปนี้ "เติมช่องว่าง" ไม่ต้องเขียนทั้งระบบ

โครงคลาสทั้งหมดเขียนไว้ให้แล้ว นิสิตแค่เติมโค้ดในจุดที่มีป้าย

```java
/* ====== fill in ... ====== */
```

และ **test เขียนครบให้แล้วใน `TestRunner.java`** — หน้าที่ของนิสิตคือเติมช่องว่างจนทุกบรรทัดเป็น `[PASS]`


รันครั้งแรกจะเห็น `[FAIL]` 16 อัน — นั่นคือรายการงาน แก้ทีละจุดจนได้ `PASS 20 / FAIL 0`

---

## รายการงาน (ทำตามลำดับ 1 → 5)

ไฟล์ที่ต้องแตะมี **9 ไฟล์** ด้านล่างนี้เท่านั้น ที่เหลือให้มาครบแล้ว (รวม core `OrderProcessor` ที่ห้ามแก้)

### Part 1 — Data Models (1 จุด)
- `Order.java` — เติม `getTotalPrice()` (จุด 1a) ให้รวมราคาสินค้าทุกชิ้น
- `Product.java` เป็น record ให้มาครบแล้ว

### Part 2 — Strategy: ส่วนลด (3 จุด)
- `PercentageDiscount.java` — เติม `applyDiscount()` ลดตามเปอร์เซ็นต์ (จุด 2a)
- `FixedDiscount.java` — เติม `applyDiscount()` ลดจำนวนคงที่ ไม่ต่ำกว่า 0 (จุด 2b)
- `OrderCalculator.java` — เติม `calculateFinalPrice()` ให้เรียกใช้ strategy (จุด 2c)

### Part 3 — Factory: การจัดส่ง (1 ไฟล์, 3 จุด)
- `ShipmentFactory.java` — เติม case `"STANDARD"`, `"EXPRESS"` และ default ที่ throw (จุด 3a–3c)
- `StandardShipment` / `ExpressShipment` ให้มาครบแล้วเป็นตัวอย่าง

### Part 4 — Decorator: บริการเสริม (2 ไฟล์)
- `GiftWrapDecorator.java` — เติม `getInfo()` + `getCost()` (จุด 4a–4b) บวก 50 บาท
- `InsuranceDecorator.java` — เติม `getInfo()` + `getCost()` (จุด 4c–4d) บวก 10% ของราคาสินค้า
- `ShipmentDecorator` (abstract) ให้มาครบแล้ว — สังเกตว่ามันมอบงานต่อให้ `wrappedShipment` โดยดีฟอลต์

### Part 5 — Observer: แจ้งเตือน (2 จุด)
- `InventoryService.java` — เติมบรรทัด print (จุด 5a)
- `EmailService.java` — เติมบรรทัด print (จุด 5b)
- `OrderProcessor.java` (subject) ให้มาครบแล้ว — **ห้ามแก้**

### (อ่านอย่างเดียว) OCP proof
ใน `TestRunner.java` มี `SmsService` เขียนไว้ให้ดูว่า observer ตัวใหม่เสียบเข้า `OrderProcessor`
เดิมได้โดยไม่แก้ core — ให้เข้าใจว่าทำไมถึงทำได้

---


## คำใบ้รวม

- ทุกจุดที่ต้องเติมมี `hint:` เขียนกำกับไว้ในโค้ดแล้ว
- ติดตรงไหน กลับไปดูสไลด์ Week 6: Strategy (7–9) · Factory (11–13) · Observer (15–17) · Decorator (19–21)
- Decorator: จำหลัก "เป็น + ถือ" — คลาส extends `ShipmentDecorator` (เป็น Shipment)
  และเรียก `wrappedShipment.getCost()` (ถือ Shipment ไว้ข้างใน) แล้วบวกส่วนของตัวเองเพิ่ม
- ทดสอบ "ต้อง throw" ใน factory ใช้แพตเทิร์น `boolean threw` + `try/catch` จาก Week 3
