const Inventory = require('../models').Inventory
const Ingredient = require('../models').Ingredient
const Sequelize = require('sequelize')
const op = Sequelize.Op

module.exports = {
    retrieve(req, res) {
        return Ingredient.findAll()
          .then(ingredient => res.status(200).send(ingredient))
          .catch(error => res.status(400).send(error))
    }
}
