document.addEventListener('keydown', function(event) {
    if (event.key === 'Enter' && event.target.tagName.toLowerCase() === 'input') {
        console.log(event.target)
        event.preventDefault();
        event.target.blur();
    }
});