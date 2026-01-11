// DOM Manipulation Basics (Frontend Fundamentals)

/* === DOM SELECTION === */
class DOMDemo {
    constructor() {
        this.setupExamples();
    }

    setupExamples() {
        console.log('=== DOM Manipulation Basics ===\n');
        
        // querySelector examples
        console.log('1. DOM Selection Methods:');
        console.log('   - document.querySelector("#myId")');
        console.log('   - document.querySelectorAll(".myClass")');
        console.log('   - document.getElementById("myId")');
        console.log('   - document.getElementsByClassName("myClass")');
    }

    /* === EVENT HANDLING === */
    addEventListeners() {
        // Click event
        const button = document.querySelector('button');
        button?.addEventListener('click', (e) => {
            console.log('Button clicked!', e.target);
        });

        // Input event
        const input = document.querySelector('input');
        input?.addEventListener('input', (e) => {
            console.log('Input value:', e.target.value);
        });

        // Form submit
        const form = document.querySelector('form');
        form?.addEventListener('submit', (e) => {
            e.preventDefault(); // Prevent page reload
            console.log('Form submitted');
        });
    }

    /* === DOM MANIPULATION === */
    manipulateDOM() {
        // Create element
        const div = document.createElement('div');
        div.className = 'my-class';
        div.id = 'my-id';
        div.textContent = 'Hello World';

        // Modify styles
        div.style.color = 'blue';
        div.style.fontSize = '16px';

        // Add to DOM
        document.body.appendChild(div);

        // Remove from DOM
        div.remove();

        // Modify attributes
        div.setAttribute('data-custom', 'value');
        console.log(div.getAttribute('data-custom'));
    }

    /* === LOCAL STORAGE === */
    demonstrateStorage() {
        // Save to localStorage
        localStorage.setItem('user', JSON.stringify({ name: 'Andres', age: 30 }));

        // Retrieve from localStorage
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        console.log('User from storage:', user);

        // Remove from localStorage
        // localStorage.removeItem('user');
    }

    /* === FETCH API === */
    async fetchData() {
        try {
            const response = await fetch('https://jsonplaceholder.typicode.com/users/1');
            const data = await response.json();
            console.log('Fetched user:', data);
            return data;
        } catch (error) {
            console.error('Fetch error:', error);
        }
    }
}

// Example HTML structure needed:
const exampleHTML = `
<!DOCTYPE html>
<html>
<head>
    <title>DOM Demo</title>
</head>
<body>
    <h1>DOM Manipulation</h1>
    
    <form id="myForm">
        <input type="text" id="nameInput" placeholder="Enter name">
        <button type="submit">Submit</button>
    </form>
    
    <div id="output"></div>
    
    <script src="dom-basics.js"></script>
</body>
</html>
`;

console.log('\nExample HTML needed:');
console.log(exampleHTML);

// Run demos (if in browser environment)
if (typeof window !== 'undefined') {
    const demo = new DOMDemo();
    demo.demonstrateStorage();
    demo.fetchData();
}
