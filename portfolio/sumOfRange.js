var testArray = [17, 42, 311, 5, 9, 10, 28, 7, 6];
var sum = 0;
var newElement = prompt("Enter a number of your choice:");

newElement = parseInt(newElement);

testArray.push(newElement);

for (var position = 0; position < testArray.length; position++) {
    sum += testArray[position];
}

console.log("The sum of " + testArray + " is: " + sum);