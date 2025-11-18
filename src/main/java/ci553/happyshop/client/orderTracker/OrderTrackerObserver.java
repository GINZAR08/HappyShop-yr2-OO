package ci553.happyshop.client.orderTracker;

import ci553.happyshop.orderManagement.OrderState;
import java.util.TreeMap;

public interface OrderTrackerObserver {
    void setOrderMap(TreeMap<Integer, OrderState> om);
}
