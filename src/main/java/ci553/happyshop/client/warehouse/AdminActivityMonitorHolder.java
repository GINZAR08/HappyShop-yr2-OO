package ci553.happyshop.client.warehouse;

/**
 * Global holder for the admin activity monitor so customer models can register with it.
 */
public class AdminActivityMonitorHolder {
    private static ci553.happyshop.client.warehouse.AdminActivityMonitor monitor;

    public static void setMonitor(ci553.happyshop.client.warehouse.AdminActivityMonitor m) {
        monitor = m;
    }

    public static ci553.happyshop.client.warehouse.AdminActivityMonitor getMonitor() {
        return monitor;
    }
}
