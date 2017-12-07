var userToJSON = {
    name: "admin",
    password: "password",
    email: "test@email.com"
};

var str = JSON.stringify(userToJSON);

userFromJSON = JSON.parse(str);

userToJSON.name = "another";
console.log(userToJSON);
console.log(userFromJSON);
console.log(userToJSON==userFromJSON);
