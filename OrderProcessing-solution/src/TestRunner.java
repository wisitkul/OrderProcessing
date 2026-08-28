import java.util.ArrayList;
import java.util.List;

/**
 * Hand-written TestRunner (Week 3 style) — no framework.
 * Run with:  java -ea TestRunner
 *
 * The tests are GIVEN. Fill in the blanks in the other files until every line is [PASS].
 */
public class TestRunner {
    private static int pass = 0, fail = 0;

    private static void check(String name, boolean ok) {
        if (ok) { pass++; System.out.println("  [PASS] " + name); }
        else    { fail++; System.out.println("  [FAIL] " + name); }
    }

    /** same as check(), but a thrown/null result counts as [FAIL] instead of crashing */
    private static void check(String name, java.util.concurrent.Callable<Boolean> cond) {
        boolean ok;
        try { Boolean r = cond.call(); ok = (r != null && r); }
        catch (Throwable e) { ok = false; }
        check(name, ok);
    }

    // capture printed output of an observer/processor for assertions
    private static String capture(Runnable r) {
        java.io.PrintStream old = System.out;
        java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(buf));
        try { r.run(); } finally { System.setOut(old); }
        return buf.toString();
    }

    private static Order sampleOrder() {
        Product a = new Product("P1", "A", 700);
        Product b = new Product("P2", "B", 300);
        return new Order("ORD-1", List.of(a, b), "u@test.com");   // total = 1000
    }

    public static void main(String[] args) {
        boolean ea = false;
        assert ea = true;
        if (!ea) System.out.println("!! -ea is OFF - assertions disabled. Run: java -ea TestRunner");

        testModels();
        testStrategy();
        testFactory();
        testDecorator();
        testObserver();
        testOcpProof();

        System.out.println("==================================");
        System.out.println("PASS " + pass + " / FAIL " + fail);
        System.out.println(fail == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED");
        if (fail > 0) System.exit(1);
    }

    /* ===== Part 1: Data Models ===== */
    static void testModels() {
        System.out.println("== Part 1: Order / Product ==");
        Order o = sampleOrder();
        check("Order.getTotalPrice() sums item prices", () -> o.getTotalPrice() == 1000.0);
        check("Order keeps its email", o.customerEmail().equals("u@test.com"));
    }

    /* ===== Part 2: Strategy ===== */
    static void testStrategy() {
        System.out.println("== Part 2: DiscountStrategy ==");
        Order o = sampleOrder();   // total 1000
        OrderCalculator calc = new OrderCalculator();

        check("PercentageDiscount 10% -> 900",
              () -> calc.calculateFinalPrice(o, new PercentageDiscount(10)) == 900.0);
        check("FixedDiscount 100 -> 900",
              () -> calc.calculateFinalPrice(o, new FixedDiscount(100)) == 900.0);
        check("FixedDiscount never goes below 0",
              () -> calc.calculateFinalPrice(o, new FixedDiscount(99999)) == 0.0);
    }

    /* ===== Part 3: Factory ===== */
    static void testFactory() {
        System.out.println("== Part 3: ShipmentFactory ==");
        ShipmentFactory f = new ShipmentFactory();

        Shipment s = f.createShipment("STANDARD");
        check("STANDARD -> cost 50",   () -> s.getCost() == 50.0);
        check("STANDARD -> info text",  () -> s.getInfo().equals("Standard Delivery"));

        Shipment e = f.createShipment("EXPRESS");
        check("EXPRESS -> cost 150",    () -> e.getCost() == 150.0);
        check("factory returns Shipment interface", () -> e instanceof Shipment);

        boolean threw = false;
        try { f.createShipment("DRONE"); }
        catch (IllegalArgumentException ex) { threw = true; }
        check("unknown type -> throws", threw);
    }

    /* ===== Part 4: Decorator ===== */
    static void testDecorator() {
        System.out.println("== Part 4: ShipmentDecorator ==");
        Order o = sampleOrder();                       // total 1000
        Shipment base = new StandardShipment();        // 50

        Shipment gift = new GiftWrapDecorator(base);   // +50
        check("GiftWrap adds 50 -> 100", () -> gift.getCost() == 100.0);
        check("GiftWrap info appends tag", () -> gift.getInfo().equals("Standard Delivery + Gift Wrapped"));

        Shipment ins = new InsuranceDecorator(base, o);// +10% of 1000 = 100
        check("Insurance adds 10% of items -> 150", () -> ins.getCost() == 150.0);
        check("Insurance info appends tag", () -> ins.getInfo().equals("Standard Delivery + Insurance"));

        // ห่อซ้อนกัน: 50 + 50 (gift) + 100 (insurance) = 200
        Shipment both = new InsuranceDecorator(new GiftWrapDecorator(base), o);
        check("stacked decorators sum up -> 200", () -> both.getCost() == 200.0);
        check("stacked decorators chain info",
              () -> both.getInfo().equals("Standard Delivery + Gift Wrapped + Insurance"));
    }

    /* ===== Part 5: Observer ===== */
    static void testObserver() {
        System.out.println("== Part 5: OrderProcessor ==");
        Order o = sampleOrder();
        OrderProcessor proc = new OrderProcessor();
        proc.register(new InventoryService());
        proc.register(new EmailService());

        String out = capture(() -> proc.processOrder(o));
        check("InventoryService notified", out.contains("Inventory updated for order ORD-1"));
        check("EmailService notified",      out.contains("Confirmation email sent to u@test.com"));

        // unregister then only one remains
        InventoryService inv = new InventoryService();
        OrderProcessor p2 = new OrderProcessor();
        p2.register(inv);
        p2.unregister(inv);
        String out2 = capture(() -> p2.processOrder(o));
        check("unregister stops notifications", !out2.contains("Inventory updated"));
    }

    /* ===== OCP proof: plug a new observer without touching the core ===== */
    static class SmsService implements OrderObserver {   // written after core
        public void update(Order order) { System.out.println("SMS sent for " + order.orderId()); }
    }

    static void testOcpProof() {
        System.out.println("== OCP: add a new observer without touching core ==");
        Order o = sampleOrder();
        OrderProcessor proc = new OrderProcessor();
        proc.register(new SmsService());
        String out = capture(() -> proc.processOrder(o));
        check("new observer works with old processor", out.contains("SMS sent for ORD-1"));
    }
}
