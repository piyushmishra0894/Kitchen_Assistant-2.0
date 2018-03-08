const Items = require('../models').Items
const Sequelize = require('sequelize')

const Promise = require('bluebird')

const op = Sequelize.Op

module.exports = {
retrieve(req, res) {
    return Items.findAll({
		
    })
      .then(Items => {
        //console.log(inventory);
        res.status(201).send(Items);
      })
      .catch(error => res.status(400).send(error))

}}
