# Admin Customer Test Page

## Overview
The Admin Customer Test Page provides a convenient way for administrators and QA testers to verify that the customer interface works correctly without having to logout and login as a customer.

## Location
The test page is accessible through the **Admin Panel (Picker & Tracker)** window under the **🧪 Customer Test** tab.

## Features

### Launch Customer Interface
- Click the "🛍️ Launch Customer Interface" button to open a new customer interface window
- The button is prominently displayed on the test page
- Multiple customer windows can be launched simultaneously for concurrent testing

### What You Can Test
The customer interface allows you to test:
- ✓ Search for products by ID or name
- ✓ View product information and images
- ✓ Add products to trolley
- ✓ View and manage trolley contents
- ✓ Proceed to checkout
- ✓ Enter billing information
- ✓ Receive receipt after purchase

### Important Notes
1. **Shared Database**: The test customer interface uses the same database as the production environment
2. **Real Orders**: Any orders placed during testing will be recorded in the system
3. **Multi-Window**: You can keep the admin panel open while testing the customer interface
4. **Concurrent Testing**: Multiple customer windows can be opened simultaneously for testing different scenarios

## How to Use

1. **Login as Admin** - Use the credentials: `admin` / `admin123`
2. **Navigate to Admin Panel** - The Picker & Tracker window opens automatically
3. **Switch to Test Tab** - Click on the **🧪 Customer Test** tab at the top of the window
4. **Launch Customer Interface** - Click the "🛍️ Launch Customer Interface" button
5. **Test Customer Features** - Use the customer interface as if you were a customer
6. **Place Test Orders** - These will create real orders in the system for testing fulfillment workflows

## Technical Details

### Files Modified
- `PickerTrackerView.java` - Added new tab for customer test page
- `CustomerTestPage.java` - New component providing the test interface

### Architecture
The test page follows the existing application architecture:
- Uses the same MVC pattern as other components
- Reuses existing `CustomerClient` class
- Integrates seamlessly with the admin panel

### Window Management
- Test customer windows are automatically registered with `WinPosManager` for proper window positioning
- Each customer window is independent and can be closed without affecting the admin panel
- Multiple instances can run simultaneously

## Testing Best Practices

1. **Test Different Product Types** - Try adding various products to trolley
2. **Test Checkout Flow** - Verify the complete checkout process works
3. **Test Edge Cases** - Try special characters in billing information, different postal codes, etc.
4. **Monitor Orders** - Switch to the "Order Map" tab to see orders appear in real-time
5. **Test Fulfillment** - After placing test orders, verify they appear in the picker interface

## Troubleshooting

### Customer Interface Won't Launch
- Check that the database connection is active
- Ensure no critical errors in the console output
- Verify the `CustomerClient` class hasn't been modified

### Database Issues
- If test orders aren't being saved, verify the database is running
- Check for any file permission issues on the database folder

### Window Issues
- If windows overlap, move them manually or close and relaunch
- The `WinPosManager` will automatically position new windows
