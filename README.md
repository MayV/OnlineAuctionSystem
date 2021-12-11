# OnlineAuctionSystem

Requirements:
1. An auction is initiated by a seller and consists of a single product. The seller can edit an auction till it is in draft state. The seller can start an auction and can define a start and end date. The auction is started once it is moved to publish state. It starts from 0$ and buyers can keep bidding (current_bid + a predefined increment) until it expires.
2. The seller can define a reserve price. Its presence is visible, but the actual amount is not. If defined and the final bid is less than the reserve price, the auction will fail with a separate status.
3. Buyers should be allowed to see any auction details including the product details which is being sold in that auction.
4. Buyers should also be allowed to subscribe to an auction i.e. they should be notified of any change in the auction. While the auction is live, buyers can bid for the product. Buyers who bid in the auction automatically become the subscriber of that auction. The seller is always a subscriber of the auction. Whenever the auction state changes, subscribers are notified.

Optional Requirements (Good to have):
1. Bidders might also make use of an autobidding system. Given a maximum value, each time the user is outbid, the system will automatically try to bid on behalf of the user with the smallest value possible(the predefined increment above will be used to reach this value; calculation must strictly follow it), up until the maximum value has been reached.

Features:
1.  Register a seller/buyer.
2.  Seller can create a new auction which will always go in DRAFT state.
3.  Seller can add product details in an auction which is in DRAFT state & can also mention the base price for the product.
4.  Seller can publish an auction given all necessary details have been added. Seller can schedule an auction while publishing the auction.
5.  Seller can view his auction details.
6.  Buyers can view any auction details.
7.  Buyers can subscribe/unsubscribe to an auction.
8.  Buyers can bid in an auction.
9.  When the auction state changes, a notification is sent to all the subscribers.
10. Sellers/Buyers can view their latest ‘x’ notifications.
