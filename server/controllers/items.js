const model = require('../models')
const Items = require('../models').Items
const Sequelize = require('sequelize')

const Promise = require('bluebird')


var db = require('../models/index.js');
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

    },

  findRecipe(req,res){
    userId = req.params.id;
    db.sequelize.query('SELECT * FROM Items WHERE Items.id NOT IN (SELECT DISTINCT temp.itemid FROM (SELECT map.itemid,\
      map.ingredientid, inv.UserId, inv.Quantity, map.quantity as q FROM ItemIngredientMappings map LEFT JOIN (SELECT * FROM Inventories WHERE UserId = :user)\
       inv ON map.IngredientId = inv.IngredientId) AS temp WHERE temp.q > temp.Quantity or temp.Quantity IS NULL)', 
    { replacements: { user: userId}, type: db.sequelize.QueryTypes.SELECT } 
    ).then(item =>{
     console.log(item)
     if(item[0] == null){
        res.status(201).send('no recipe available with given ingredients');
       //console.log("no matching recipe")
      } 
      else {
     res.status(201).send(item);
     }
    })
  }


}
