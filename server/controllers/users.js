const User = require('../models').User;
const UserDetail = require('../models').UserDetails;
module.exports = {
  create(req, res) {
    return User
      .create({
        firstName: req.body.firstName,
	   lastName: req.body.lastName,
	    email: 	req.body.email,
      }).then(user => {
        console.log(user)
        UserDetail.create({
          Age: req.body.Age,
          Sex: req.body.Sex,
          Nationality: req.body.Nationality,
          CurrentCity: req.body.CurrentCity,
          CurrentCountry: req.body.CurrentCountry,
          UserID: user.id,
        })
      })
      .then(UserDetails => {
        console.log(UserDetails);
        res.status(201).send("User Created");
      }).catch(error => res.status(400).send(error))

  },
};
