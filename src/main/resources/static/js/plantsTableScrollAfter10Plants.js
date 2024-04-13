document.addEventListener("DOMContentLoaded", function() {
    const table = document.getElementById("plantTable");
    let height = table.querySelector("thead").getBoundingClientRect().height;

    for (let i = 0; i < 10; i++) {
        if (table.rows[i]) {
            height += table.rows[i].getBoundingClientRect().height;
        }
    }

    const tableContainer = document.getElementById("plantTableContainer");
    tableContainer.style.maxHeight = height + "px";
});