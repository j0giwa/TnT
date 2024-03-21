document.addEventListener("DOMContentLoaded", function() {
  function changeActive(index) {
      // Alle Navlinks auswählen und die Klasse "active" entfernen
      var navlinks = document.getElementsByClassName("navlink");
      for (var i = 0; i < navlinks.length; i++) {
        navlinks[i].classList.remove("active");
      }
      
      // Dem ausgewählten Link die Klasse "active" hinzufügen
      navlinks[index-1].classList.add("active");
  }
});