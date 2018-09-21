# Software Developer Coding Challenge

This is a coding challenge for software developer applicants applying through http://work.traderev.com/

## Goal:

#### You have been tasked with building a simple online car auction system which will allow users to bid on cars for sale and with the following funcitionalies: 

  - [ ] Fork this repo. Keep it public until we have been able to review it.
  - [ ] A simple auction bidding system
  - [ ] Record a user's bid on a car
  - [ ] Get the current winning bid for a car
  - [ ] Get a car's bidding history 

 ### Bonus:

  - [ ] Unit Tests on the above functions
  - [ ] Develop a UI on web or mobile or CLI to showcase the above functionality

## Evaluation:

 - [ ] Solution compiles. Provide a README to run/build the project and explain anything that you leave aside
 - [ ] No crashes, bugs, compiler warnings
 - [ ] App operates as intended
 - [ ] Conforms to SOLID principles
 - [ ] Code is easily understood and communicative
 - [ ] Commit history is consistent, easy to follow and understand

# Simple Auction Bidding System Solution:

## Deploy:
```
git clone git@github.com:xuerongCode/software-developer-coding-challenge.git
cd software-developer-coding-challenge
cd server
sh runApp.sh
```

## How to Play:
  - [ ] Use api to find all users in the system. All users can bid for Auction.
  - [ ] Use api to find all vehicles in the system. Record the auctionId for later using to bid
  - [ ] There are two auctions in the system. One will expire after one hour, and the other one will expire after 5 minutes.
  - [ ] For bidding an auction, you need to provide userId, auctionId, amount, and currency.
  - [ ] There is CLI which support all APIS (/software-developer-coding-challenge/server/runApp.sh).

## CLI:
```
  --users : Get all users."
	eg: sh run.sh --users"
  --vehicles : Get all vehicles."
	eg: sh run.sh --vehicles"
  --auctions : Get all auctions."
	eg: sh run.sh --auctions"
  --bids : Get all bids."
	eg: sh run.sh --bids"
  --userById {userId} : Find User by userId."
      eg: sh run.sh --userById 1"
  --vehicleById {vehicleId} : Find vehicle by vehicleId."
      eg: sh run.sh --vehicleById 1"
  --auctionById {auctionId} : Find auction by auctionId."
      eg: sh run.sh --auctionById 1"
  --bidById {bidId} : Find bid by bidId."
      eg: sh run.sh --bidById 1"
  --bidsOfUser {userId} : Find all bids on a user."
      eg: sh run.sh --bidsOfUser 1"
  --bidsOfVehicle {vehicleId} : Find all bids on a vehicle."
      eg: sh run.sh --bidsOfVehicle 1"
  --bidsOfAuction {auctionId} : Find all bids on an auction."
      eg: sh run.sh --bidsOfAuction 1"
  --userBidsOnVehicle {userId} {vehicleId} : Find a user's bids on a Vehicle."
      eg: sh run.sh --userBidsOnVehicle 1 1"
  --currentWinUserOfAuction {auctionId} : Find current win user of a auction."
      eg: sh run.sh --currentWinUserOfAuction 1"
  --currentWinBidOfAuction {auctionId} : Find current win bid of a auction."
      eg: sh run.sh --currentWinBidOfAuction 1"
  --currentWinBidOfVehicle {vehicleId} : Find current win bid of a vehicle."
      eg: sh run.sh --currentWinBidOfVehicle 1"
  --applyBid {userId} {auctionId} {amount} {currency} : Post bid for an auction."
      eg: sh run.sh --applyBid 1 1 11.11 CAD"
```
## Future improvements:
  - [ ] Cache auction to reduce database access.
  - [ ] Instead of synchronizing whole auction service, synchronizing each auction wll improve performance.
  - [ ] Move sensitive information (like userId) from URL into request head.
  - [ ] Adding authentication service inside service layer.
  - [ ] More detailed exception handler to handle different errors

## API:

#### Find all users:
```
GET http://localhost:8080/user/

...
Example Response:
[
    {
        "_class": "User",
        "id": 1,
        "name": "Xuerong"
    },
    {
        "_class": "User",
        "id": 2,
        "name": "Mansi"
    }
]
```

#### Find all Vehicles:
```
GET http://localhost:8080/vehicle/

...
Example Response:
[
    {
        "_class": "Vehicle",
        "id": 1,
        "name": "Ford-150 raptor",
        "imageUrl": "raptorUrl",
        "price": {
            "_class": "Amount",
            "amount": 90000.55,
            "currency": "CAD"
        },
        "auctionId": "1"
    },
    {
        "_class": "Vehicle",
        "id": 2,
        "name": "Honda typeR",
        "imageUrl": "typeRUrl",
        "price": {
            "_class": "Amount",
            "amount": 45000.11,
            "currency": "USD"
        },
        "auctionId": "2"
    }
]
```

#### Find all auctions:
```
GET http://localhost:8080/auction/

...
Example Response:
[
    {
        "_class": "Auction",
        "id": 1,
        "startAt": "1969-12-31 19:00:00.0",
        "duration": 1800000,
        "vehicleId": 1,
        "currentWinUserId": null,
        "currentWinBidId": null
    },
    {
        "_class": "Auction",
        "id": 2,
        "startAt": "1969-12-31 19:02:00.0",
        "duration": 600000,
        "vehicleId": 2,
        "currentWinUserId": null,
        "currentWinBidId": null
    }
]
```

#### Find all bids:
```
GET http://localhost:8080/bid

...
Example Response:
[
    {
        "_class": "Bid",
        "id": 3,
        "amount": {
            "_class": "Amount",
            "amount": 3,
            "currency": "CAD"
        },
        "userId": 1,
        "auctionId": 1
    }
]
```

#### Find user by Id:
```
GET http://localhost:8080/user/{userId}

...
Example Response:
{
    "_class": "User",
    "id": 1,
    "name": "Xuerong"
}
```

#### Find vehicle by Id:
```
GET http://localhost:8080/vehicle/{vehicleId}

...
Example Response:
{
    "_class": "Vehicle",
    "id": 1,
    "name": "Ford-150 raptor",
    "imageUrl": "raptorUrl",
    "price": {
        "_class": "Amount",
        "amount": 90000.55,
        "currency": "CAD"
    },
    "auctionId": "1"
}
```

#### Find auction by Id:
```
GET http://localhost:8080/auction/{auctionId}

...
Example Response:
{
    "_class": "Auction",
    "id": 1,
    "startAt": "1969-12-31 19:00:00.0",
    "duration": 1800000,
    "vehicleId": 1,
    "currentWinUserId": null,
    "currentWinBidId": null
}
```

#### Find bid by Id:
```
GET http://localhost:8080/bid/{bidId}

...
Example Response:
{
    "_class": "Bid",
    "id": 3,
    "amount": {
        "_class": "Amount",
        "amount": 3,
        "currency": "CAD"
    },
    "userId": 1,
    "auctionId": 1
}
```

#### Select all bids of a User:
```
GET http://localhost:8080/user/{userId}/bid

...
Example Response:
[
    {
        "_class": "Bid",
        "id": 3,
        "amount": {
            "_class": "Amount",
            "amount": 3,
            "currency": "CAD"
        },
        "userId": 1,
        "auctionId": 1
    }
]
```

#### Select all bids of a vehicle:
```
GET http://localhost:8080/vehicle/{vehicleId}/bid
```

#### Select all bids of a auction:
```
GET http://localhost:8080/auction/{auctionId}/bid

```

#### Select all bids of a vehicle on a user:
```
GET http://localhost:8080/user/{userId}/vehicle/{vehicleId}/bid

```

#### Select current winning bid for an auction:
```
GET http://localhost:8080/auction/{auctionId}/currentWinBid

...
Example Response:
{
    "_class": "Bid",
    "id": 3,
    "amount": {
        "_class": "Amount",
        "amount": 3,
        "currency": "CAD"
    },
    "userId": 1,
    "auctionId": 1
}
```

#### Select current winning user for an auction:
```
GET http://localhost:8080/auction/{auctionId}/currentWinUser

...
Example Response:
{
    "_class": "User",
    "id": 1,
    "name": "Xuerong"
}
```

#### Select current winning bid for a vehicle:
```
GET http://localhost:8080/vehicle/{vehicleId}/currentWinBid

...
Example Response:
{
    "_class": "Bid",
    "id": 3,
    "amount": {
        "_class": "Amount",
        "amount": 3,
        "currency": "CAD"
    },
    "userId": 1,
    "auctionId": 1
}
```

#### Bid a Auction:
```
Post http://localhost:8080/auction/{auctionId}
Content-Type: application/json
userAuth: {userId}

...
Example Request:
{
	"amount": 5,
	"currency": "CAD"
}

...
Example Response:
{
    "_class": "Bid",
    "id": 4,
    "amount": {
        "_class": "Amount",
        "amount": 5,
        "currency": "CAD"
    },
    "userId": 2,
    "auctionId": 1
}
```