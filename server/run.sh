#!/bin/sh

function showAllUsers() {
	echo "curl -X GET http://localhost:8080/user"
	echo `curl -X GET http://localhost:8080/user`
}

function showAllVehicles() {
	echo "curl -X GET http://localhost:8080/vehicle"
	echo `curl -X GET http://localhost:8080/vehicle`
}

function showAllAuctions() {
	echo "curl -X GET http://localhost:8080/auction"
	echo `curl -X GET http://localhost:8080/auction`
}

function showAllBids() {
	echo "curl -X GET http://localhost:8080/bid"
	echo `curl -X GET http://localhost:8080/bid`
}

function showUserById() {
	echo "curl -X GET http://localhost:8080/user/$1"
	echo `curl -X GET http://localhost:8080/user/$1`
}

function showVehicleById() {
	echo "curl -X GET http://localhost:8080/vehicle/$1"
	echo `curl -X GET http://localhost:8080/vehicle/$1`
}

function showAuctionById() {
	echo "curl -X GET http://localhost:8080/auction/$1"
	echo `curl -X GET http://localhost:8080/auction/$1`
}

function showBidById() {
	echo "curl -X GET http://localhost:8080/bid/$1"
	echo `curl -X GET http://localhost:8080/bid/$1`
}

function showBidsByUser() {
	echo "curl -X GET http://localhost:8080/user/$1/bid"
	echo `curl -X GET http://localhost:8080/user/$1/bid`
}

function showBidsByAuction() {
	echo "curl -X GET http://localhost:8080/auction/$1/bid"
	echo `curl -X GET http://localhost:8080/auction/$1/bid`
}

function showBidsByVehicle() {
	echo "curl -X GET http://localhost:8080/vehicle/$1/bid"
	echo `curl -X GET http://localhost:8080/vehicle/$1/bid`
}

function showUserBidsOnVehicle() {
	echo "curl -X GET http://localhost:8080/user/$1/vehicle/$2/bid"
	echo `curl -X GET http://localhost:8080/user/$1/vehicle/$2/bid`
}

function showCurrentWinUserOfAuction() {
	echo "curl -X GET http://localhost:8080/auction/$1/currentWinUser"
	echo `curl -X GET http://localhost:8080/auction/$1/currentWinUser`
}

function showCurrentWinBidOfAuction() {
	echo "curl -X GET http://localhost:8080/auction/$1/currentWinBid"
	echo `curl -X GET http://localhost:8080/auction/$1/currentWinBid`
}

function showCurrentWinBidOfVehicle() {
	echo "curl -X GET http://localhost:8080/vehicle/$1/currentWinBid"
	echo `curl -X GET http://localhost:8080/vehicle/$1/currentWinBid`
}

function applyBid() {
	echo "curl -X POST http://localhost:8080/auction/$1 \n
  -H 'Cache-Control: no-cache' \n
  -H 'Content-Type: application/json' \n
  -H 'userAuth: $2' \n
  -d '{
    'amount': $3,
    'currency': '$4'
  }'
"
	echo `curl -X POST http://localhost:8080/auction/$1 \
  			-H 'Cache-Control: no-cache' \
  			-H 'Content-Type: application/json' \
  			-H "userAuth: $2" \
  			-d '{
				"amount": '$3',
				"currency": "'$4'"
			}'
		`
}

function usage {
	echo "The CLI requires curl."
    echo "Options are:"
    echo "  --users : Get all users."
	echo "      eg: sh run.sh --users"
    echo "  --vehicles : Get all vehicles."
	echo "      eg: sh run.sh --vehicles"
    echo "  --auctions : Get all auctions."
	echo "      eg: sh run.sh --auctions"
    echo "  --bids : Get all bids."
	echo "      eg: sh run.sh --bids"
    echo "  --userById {userId} : Find User by userId."
	echo "      eg: sh run.sh --userById 1"
	echo "  --vehicleById {vehicleId} : Find vehicle by vehicleId."
	echo "      eg: sh run.sh --vehicleById 1"
	echo "  --auctionById {auctionId} : Find auction by auctionId."
	echo "      eg: sh run.sh --auctionById 1"
	echo "  --bidById {bidId} : Find bid by bidId."
	echo "      eg: sh run.sh --bidById 1"
	echo "  --bidsOfUser {userId} : Find all bids on a user."
	echo "      eg: sh run.sh --bidsOfUser 1"
	echo "  --bidsOfVehicle {vehicleId} : Find all bids on a vehicle."
	echo "      eg: sh run.sh --bidsOfVehicle 1"
	echo "  --bidsOfAuction {auctionId} : Find all bids on an auction."
	echo "      eg: sh run.sh --bidsOfAuction 1"
	echo "  --userBidsOnVehicle {userId} {vehicleId} : Find a user's bids on a Vehicle."
	echo "      eg: sh run.sh --userBidsOnVehicle 1 1"
	echo "  --currentWinUserOfAuction {auctionId} : Find current win user of a auction."
	echo "      eg: sh run.sh --currentWinUserOfAuction 1"
	echo "  --currentWinBidOfAuction {auctionId} : Find current win bid of a auction."
	echo "      eg: sh run.sh --currentWinBidOfAuction 1"
	echo "  --currentWinBidOfVehicle {vehicleId} : Find current win bid of a vehicle."
	echo "      eg: sh run.sh --currentWinBidOfVehicle 1"
	echo "  --applyBid {userId} {auctionId} {amount} {currency} : Post bid for an auction."
	echo "      eg: sh run.sh --applyBid 1 1 11.11 CAD"
}

re='^[0-9]+$'
doubleRe='^[0-9]*\.[0-9]+|[0-9]+$'
currencyRe='^(CAD|USD)$'

userId=0
auctionId=0
bidId=0
vehicleId=0
amount=0
currency=0

SHOW_ALL_USERS=0
SHOW_ALL_VEHICLE=0
SHOW_ALL_AUCTION=0
SHOW_ALL_BID=0

USER_BY_ID=0
VEHICLE_BY_ID=0
AUCTION_BY_ID=0
BID_BY_ID=0

SHOW_BIDS_BY_USER=0
SHOW_BIDS_BY_AUCTION=0
SHOW_BIDS_BY_VEHICLE=0

SHOW_USER_BIDS_ON_VEHICLE=0

SHOW_CURRENT_WIN_USER_OF_AUCTION=0
SHOW_CURRENT_WIN_BID_OF_AUCTION=0
SHOW_CURRENT_WIN_USER_OF_VEHICLE=0

APPLY_BID=0

while true ; do
	case "$1" in
		--users)
			SHOW_ALL_USERS=1
			break ;;
		--vehicles)
			SHOW_ALL_VEHICLE=1
			break ;;
		--auctions)
			SHOW_ALL_AUCTION=1
			break ;;
		--bids)
			SHOW_ALL_BID=1
			break ;;
		--userById)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				userId=$2
				USER_BY_ID=1
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			break ;;
		--vehicleById)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				vehicleId=$2
				VEHICLE_BY_ID=1
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			break ;;
		--auctionById)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				auctionId=$2
				AUCTION_BY_ID=1
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			break ;;
		--bidById)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				bidId=$2
				BID_BY_ID=1
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			break ;;
		--bidsOfUser)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				userId=$2
				SHOW_BIDS_BY_USER=1
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			break ;;
		--bidsOfVehicle)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				vehicleId=$2
				SHOW_BIDS_BY_VEHICLE=1
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			break ;;
		--bidsOfAuction)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				auctionId=$2
				SHOW_BIDS_BY_AUCTION=1
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			break ;;
		--userBidsOnVehicle)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				userId=$2
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			
			if [[ $3 =~ $re && $3 != 0 ]] ; then
				auctionId=$3
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi

			if [[ $userId != 0 && $auctionId != 0 ]] ; then
				SHOW_USER_BIDS_ON_VEHICLE=1
			else
				echo "error: miss argument" >&2; exit 1
			fi
			break ;;
		--currentWinUserOfAuction)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				SHOW_CURRENT_WIN_USER_OF_AUCTION=1
				auctionId=$2
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			break ;;
		--currentWinBidOfAuction)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				SHOW_CURRENT_WIN_BID_OF_AUCTION=1
				auctionId=$2
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			break ;;
		--currentWinBidOfVehicle)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				SHOW_CURRENT_WIN_USER_OF_VEHICLE=1
				vehicleId=$2
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			break ;;
		--applyBid)
			if [[ $2 =~ $re && $2 != 0 ]] ; then
				userId=$2
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			
			if [[ $3 =~ $re && $3 != 0 ]] ; then
				auctionId=$3
			else
				echo "error: Not a number that is greater than zero" >&2; exit 1
			fi
			
			if [[ ( $4 =~ $re && $4 != 0 ) || ( $4 =~ $doubleRe ) ]] ; then
				amount=$4
			else
				echo "error: Amount must be a double." >&2; exit 1
			fi
			
			if [[ $5 =~ $currencyRe ]] ; then
				currency=$5
			else
				echo "error: Currency must be CAD or USD" >&2; exit 1
			fi

			if [[ $userId != 0 && $auctionId != 0 && $amount != 0 && $currency != 0 ]] ; then
				APPLY_BID=1
			else
				echo "error: miss argument" >&2; exit 1
			fi
			break ;;
		--help)
			usage
			break ;;									
		--) break ;;
        *) echo "Internal error! use --help to find option" ; exit 1 ;;
	esac
done

if [  "$SHOW_ALL_USERS" -eq 1 ]; then
	showAllUsers
elif [ "$SHOW_ALL_VEHICLE" -eq 1 ]; then
	showAllVehicles
elif [ "$SHOW_ALL_AUCTION" -eq 1 ]; then
	showAllAuctions
elif [ "$SHOW_ALL_BID" -eq 1 ]; then
	showAllBids
elif [ "$USER_BY_ID" -eq 1 ]; then
	showUserById $userId
elif [ "$VEHICLE_BY_ID" -eq 1 ]; then
	showVehicleById $vehicleId
elif [ "$AUCTION_BY_ID" -eq 1 ]; then
	showAuctionById $auctionId
elif [ "$BID_BY_ID" -eq 1 ]; then
	showBidById $bidId
elif [ "$SHOW_BIDS_BY_USER" -eq 1 ]; then
	showBidsByUser $userId
elif [ "$SHOW_BIDS_BY_VEHICLE" -eq 1 ]; then
	showBidsByVehicle $vehicleId
elif [ "$SHOW_BIDS_BY_AUCTION" -eq 1 ]; then
	showBidsByAuction $auctionId
elif [ "$SHOW_USER_BIDS_ON_VEHICLE" -eq 1 ]; then
	showUserBidsOnVehicle $userId $auctionId
elif [ "$SHOW_CURRENT_WIN_USER_OF_AUCTION" -eq 1 ]; then
	showCurrentWinUserOfAuction $auctionId
elif [ "$SHOW_CURRENT_WIN_BID_OF_AUCTION" -eq 1 ]; then
	showCurrentWinBidOfAuction $auctionId
elif [ "$SHOW_CURRENT_WIN_USER_OF_VEHICLE" -eq 1 ]; then
	showCurrentWinBidOfVehicle $vehicleId
elif [ "$APPLY_BID" -eq 1 ]; then
	applyBid $auctionId $userId $amount $currency
fi