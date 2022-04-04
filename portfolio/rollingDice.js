var numSides = prompt("How many sides does the die have?");
var numdices = prompt("How many dices you want to play?");
function rollDice(numSides) {
    return Math.floor(Math.random() * numSides) + 1;
}

for (var i = 0; i < numdices; i++) {
    console.log(rollDice(numSides));
}