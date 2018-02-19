const Recipe = require('../models').Recipe;

module.exports = {
  create(req, res) {
    return Recipe
      .create({
        Name: req.body.Name,
        Step: req.body.Step,
        Ingredient:  req.body.Ingredient,
      })
      .then(recipe => res.status(201).send(recipe))
      .catch(error => res.status(400).send(error));
  },
};
