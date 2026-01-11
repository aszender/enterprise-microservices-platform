

// Function Declaration and Arrow Function
function add (a,b){
    return a + b;
}
const add2 = (a,b) => a + b;
console.log(add(2,3));

// Template Literals
const greet = (name) => `Hello, ${name}!`;
console.log(greet("Andres"));

// Destructuring Assignment
const user = {
    name: "Andres",
    age: 30,
};
const {name: userName, age: userAge} = user;
console.log(`Name: ${userName}, Age: ${userAge}`);

// Array Methods with Arrow Functions
const numbers = [1,2,3,4,5];
const squaredNumbers = numbers.map(n => n * n);
console.log(squaredNumbers);
const evens = numbers.filter(n => n % 2 === 0);
console.log(evens);
const sum = numbers.reduce((acc, n) => acc + n, 0);
console.log(sum);

// Rest and Spread Operators
const nums = [10,20,30,40,50];
const [first, second, ...rest] = nums;
console.log(`First: ${first}, Second: ${second}, Rest: ${rest}`);

//Async/Await
const fetchData = async () => {
  try {
    const response = await fetch('https://jsonplaceholder.typicode.com/posts/1');
    if (!response.ok) {
      throw new Error(`HTTP error ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error(error);
    throw error; // important
  }
};

fetchData().then(data => console.log(data));
