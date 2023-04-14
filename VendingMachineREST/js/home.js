$(document).ready(function() {
    loadItems();
});

/**
 * Loads and displays the vending machine items
 */
function loadItems() {
    $.ajax({
        type: 'GET',
        url: 'http://vending.us-east-1.elasticbeanstalk.com/items',
        success: function(items) {
            $('#items').empty();
            $.each(items, function(index, item) {
                addItem(index, item);
            });
        },
        error: function(xhr) {
            $('#items').text(xhr.responseJSON.message);
        }
    });
}

/**
 * Adds each vending machine item to the display
 * @param index , the index of the current item
 * @param data , the data for the current item
 */
function addItem(index, data) {
    var entry = '<div class="col-sm-4 d-flex justify-content-around">';
        entry += '<div class="card">';
        entry += '<div>' + data.id + '</div>';
        entry += '<div>' + data.name + '</div>';
        entry += '<div>' + displayPrice(data.price) + '</div>';
        entry += '<div>Quantity Left: ' + data.quantity + '</div>';
        entry += '</div>';
        entry += '</div>';
    $('#items').append(entry);
}

var cash = 0;
/**
 * Adds cash to the vending machine credit amount. Called by the Add cash buttons.
 * @param cents , the amount of cash to add in cents
 */
function addCash(cents) {
    cash += cents;
    $('#credit').text(displayPrice(cash / 100));
}

/**
 * Makes a purchase from the vending machine. Called by the Make Purchase button.
 */
function makePurchase() {
    $.ajax({
        type: 'POST',
        url: 'http://vending.us-east-1.elasticbeanstalk.com/money/' + cash / 100 + '/item/' + $('#itemId').val(),
        success: function(change) {
            loadItems();
            $('#messages').text('Thank You!!!');
            displayChange(change);
        },
         error: function(xhr) {
            $('#messages').text(xhr.responseJSON.message);
            $('#messages').css("color","red");
        }
        
    });
}

/**
 * Returns the correct amount of change, converts the credit amount to quantities of coin denominations.
 */
function returnChange() {
    var coins = { 'quarters': 25, 'dimes': 10, 'nickels': 5, 'pennies': 1 };
    var change = {};

    for (var coin in coins) {
        change[coin] = Math.floor(cash / coins[coin]);
        cash %= coins[coin];
    }

    displayChange(change);
}

/**
 * Displays the correct amount of change..
 * @param change the quantities of each coin denomination
 */
function displayChange(change) {
    var display = '&nbsp;';
    for (var coin in change) {
        if (change[coin] > 1) {
            display += change[coin] + ' ' + coin + '&nbsp;';
        }
        if (change[coin] === 1) {
            if (coin === 'pennies') {
                display += change[coin] + ' ' + coin.slice(0, -3) + '&nbsp;';
            }
            else {
                display += change[coin] + ' ' + coin.slice(0, -1) + '&nbsp;';
            }
        }
    }
    cash = 0;
    $('#credit').text(displayPrice(cash));
    $('#change').html(display);
}

/**
 * Formats numbers as currency
 * @param money the input number to format
 * @returns {string} the number formatted as currency
 */
function displayPrice(money) {
    return '$' + String(money.toFixed(2));
}



