# Nike Store 

![3 Light Mode](https://github.com/amirtashakkori/Nike-Store/assets/110338407/531b6b12-9030-400e-9dae-36111580c136)

Nike Store is a comprehensive shoe shopping application that, by sending requests to an API via Retrofit and RxJava libraries, fetches and displays a list of products in four categories: newest, most viewed, most expensive, and least expensive. Each product includes complete information and a list of user reviews. The homepage, in addition to the product list, includes product banners and a search box.

Each product can be added to the shopping cart and wish list, and the app allows for commenting on products.

This project includes three fragments: Home, Cart, and Profile:

Cart Fragment: Contains a list of products stored in your cart. At the bottom, you can view the receipt for the products in your cart. Additionally, you can increase, decrease, or remove the items in your cart. After confirming the selected items, you can proceed to the payment gateway to finalize your order.
Profile Fragment: Includes options for the wish list, order history, and logging in or out of your user account.
Due to its relatively high functionality, this project involves various challenges, such as sending requests to the web service, receiving information in the desired format, optimizing the app, integrating with the payment gateway, and implementing a local database to store the wish list.

Note: To access the cart and order history, you must log in to your user account.

Features:

- API Requests: Sending requests to the API via Retrofit and RxJava libraries, including constructing sliders for product banners.
- Product Listing: Fetching and displaying the list of products.
- Product Sorting: Creating a spinner to sort products based on desired factors.
- Search Box: Searching for specific products.
- Product Detail Page: Displaying complete product information.
- Add to Cart and Wish List: Adding products to the shopping cart and wish list.
- User Reviews: Fetching and displaying user reviews for each product, and allowing users to add their reviews.
- User Authentication: Login and registration pages.
- Cart Management: Refreshing the cart after each change, with real-time updates of selected items and the receipt.
- Payment Gateway Integration: Navigating to the payment gateway and adding the order to the order history.
- Local Database: Implementing a local database using the Room library to save the wish list.
- User Account Management: Buttons for logging in and out of user accounts.

  
This project includes both light and dark modes.
