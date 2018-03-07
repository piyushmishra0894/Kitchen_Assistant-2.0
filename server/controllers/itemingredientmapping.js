const ItemIngredientMapping = require('../models').ItemIngredientMapping
const Items = require('../models').Items
const Ingredients = require('../models').Ingredients
const Sequelize = require('sequelize')

const Promise = require('bluebird')

const op = Sequelize.Op

module.exports = {
retrieve(req, res) {
    return ItemIngredientMapping.findAll({
		
    })
      .then(itemingredientmapping => {
        //console.log(inventory);
        res.status(201).send(itemingredientmapping);
      })
      .catch(error => res.status(400).send(error))

}}
