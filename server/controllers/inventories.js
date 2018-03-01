const Inventory = require('../models').Inventory
const Ingredient = require('../models').Ingredient
const Sequelize = require('sequelize')
const op = Sequelize.Op

module.exports = {
  create(req, res) {
    return Inventory
      .create({
        UserId: req.body.UserId,
        Itemid: req.body.Itemid,
        Quantity: req.body.Quantity
      })
      .then(inventory => res.status(201).send(inventory))
      .catch(error => res.status(400).send(error))
  },
  retrieve(req, res) { /*
    return Inventory.findAll({
      include: [Ingredient],
      where: {
       UserId: req.params.id
      }})
    .then(inventory => res.status(202).send(inventory))
    .catch(error => res.status(400).send(error))
    */

    var inventory = Inventory.findAll({
      where: {
        UserId: req.params.id
      }
    // include: [Ingredient]
    })
      .then(inventory => {
        var arry = [];
        inventory.forEach(function (value, index) {
           Ingredient.findOne({
              where: {id: value.Itemid
            }})
              .then(ingredients => {
                const resObj = inventory.map(value => {
                  return Object.assign(
                    {},
                    {
                      id: ingredients.id,
                      UserId: inventory.UserId,
                      name: ingredients.name
                    }
                  )
                });
              
                res.json(resObj);
              })
              .catch(error => {
                console.log(error);
                res.status(401).send(error);})
        });
        //res.status(202).write(value);
        //res.status(202).write(inventory);
        return inventory;
      })
      .catch(error => {
        console.log(error);
        res.status(400).send(error);
      });
      //res.end();
      //res.status(202).send(inventory);
  /*
      return Ingredient.findAll({
        where: {id: {
            [op.in]: [1,2,3]
          } 
        }})
        .then(ingredientList => res.status(202).send(inventory))
        .catch(error => res.status(400).send(error));*/
  }

}