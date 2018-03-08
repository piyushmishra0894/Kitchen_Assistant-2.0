const Items = require('../models').Items
const ItemRecipes = require('../models').ItemRecipes
const Sequelize = require('sequelize')

const Promise = require('bluebird')

const op = Sequelize.Op

module.exports = {
retrieve(req, res) {
    return ItemRecipes.findAll({
      where: {
        itemid: req.params.id
    }})
      .then(itemrecipes => {
        //console.log(inventory);
        res.status(201).send(itemrecipes);
      })
      .catch(error => res.status(400).send(error))
	}
}