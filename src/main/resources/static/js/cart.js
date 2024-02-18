// function updateQuantity(button, change) {
//     var row = button.closest('tr');
//     var quantityElement = row.querySelector('[id^="quantity-"]');
//     var priceElement = row.querySelector('[id^="price-"]');
//     var totalPriceElement = row.querySelector('[id^="total-"]');
//     if (quantityElement) {
//         var currentQuantity = parseInt(quantityElement.textContent);
//         var price = parseFloat(priceElement.textContent);
//         var newQuantity = currentQuantity + change;
//
//         if (newQuantity >= 0) {
//             var updateUrl = row.dataset.updateUrl;
//             $.ajax({
//                 url: updateUrl,
//                 type: 'POST',
//                 data: {quantity: newQuantity},
//                 success: function(data) {
//                     quantityElement.textContent = newQuantity;
//                     totalPriceElement.textContent = (newQuantity * price).toFixed(2);
//                 },
//                 error: function(xhr, status, error) {
//                     console.error("Error occurred:", error);
//                 }
//             });
//         }
//     } else {
//         console.error("Quantity element not found in the current row.");
//     }
// }
function updateQuantity(button, change) {
    var row = button.closest('tr');
    var quantityElement = row.querySelector('[id^="quantity-"]');
    var priceElement = row.querySelector('[id^="price-"]');
    var totalPriceElement = row.querySelector('[id^="total-"]');
    if (quantityElement) {
        var currentQuantity = parseInt(quantityElement.textContent);
        var price = parseFloat(priceElement.textContent);
        var newQuantity = currentQuantity + change;

        if (newQuantity >= 0) {
            var updateUrl = row.dataset.updateUrl;
            var removeUrl = row.dataset.removeUrl;
            $.ajax({
                url: updateUrl,
                type: 'POST',
                data: {quantity: newQuantity},
                success: function(data) {
                    if (newQuantity === 0) {
                        // If quantity becomes zero, remove the item from database
                        $.ajax({
                            url: removeUrl,
                            type: 'GET', // Or 'POST' depending on your backend implementation
                            success: function() {
                                // On successful removal from database, remove the row from DOM
                                row.remove();
                            },
                            error: function(xhr, status, error) {
                                console.error("Error occurred while removing item:", error);
                            }
                        });
                    } else {
                        quantityElement.textContent = newQuantity;
                        totalPriceElement.textContent = (newQuantity * price).toFixed(2);
                    }
                },
                error: function(xhr, status, error) {
                    console.error("Error occurred:", error);
                }
            });
        }
    } else {
        console.error("Quantity element not found in the current row.");
    }
}
