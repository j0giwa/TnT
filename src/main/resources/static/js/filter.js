
document.addEventListener("DOMContentLoaded", function() {
    // Get the dropdown menu
    var dropdownMenu = document.querySelector(".dropdown-menu");

    // Get all the dropdown items
    var dropdownItems = dropdownMenu.querySelectorAll(".dropdown-item");

    // Attach a click event listener to each dropdown item
    dropdownItems.forEach(function(item) {
      item.addEventListener("click", function() {
        // Get the text of the clicked item
        var text = item.innerHTML;

        // Perform the desired action based on the selected item
        if (text === "lecture") {
          // Code for handling lecture
          console.log("Handle lecture");
        } else if (text === "litterature") {
          // Code for handling literature
          console.log("Handle literature");
        } else if (text === "misc") {
          // Code for handling misc
          console.log("Handle misc");
        }

        // Close the dropdown menu after clicking an item
        dropdownMenu.classList.remove("show");
      });
    });
  });
