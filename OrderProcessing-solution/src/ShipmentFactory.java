/** FACTORY (Part 3): จุดเดียวที่รู้จัก concrete Shipment ทุกชนิด */
public class ShipmentFactory {
    /**
     *   createShipment("STANDARD") -> StandardShipment
     *   createShipment("EXPRESS")  -> ExpressShipment
     *   unknown type -> IllegalArgumentException
     */
    public Shipment createShipment(String type) {
        if (type == null) throw new IllegalArgumentException("type must not be null");
        return switch (type.toUpperCase()) {
            case "STANDARD" -> new StandardShipment() ;
            case "EXPRESS" -> new ExpressShipment() ;
            default -> throw new IllegalArgumentException();
        };
    }
}
