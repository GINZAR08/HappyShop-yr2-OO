package ci553.happyshop.client.customer;

/**
 * Observer interface for admin to receive real-time customer activity updates.
 */
public interface AdminObserver {
    void onCustomerActivity(String imageName, String searchResult, String trolley, String receipt);
}
