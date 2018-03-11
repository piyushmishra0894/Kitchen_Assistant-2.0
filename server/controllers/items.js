//import { isNullOrUndefined } from 'util';
const _ = require("util")
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
  },
  findRecipebWithTags(req, res){
      tags = req.query;
      timeToPrepare = req.query.timeToPrepare;
      calories = req.query.calories;
     // if (_.isNull(timeToPrepare) || isEmpty(timeToPrepare)){
     //   timeToPrepare = Number.MAX_SAFE_INTEGER
     // }
      //console.log(tags);
      if(!timeToPrepare){
        timeToPrepare = Number.MAX_SAFE_INTEGER
      }
      if(!calories){
        calories = Number.MAX_SAFE_INTEGER
      }

      Items.findAll({
        where: {
          [op.and]:{
          [op.or]: [{Appetizer: tags.Appetizer}, {brunch: tags.brunch}, {dinner: tags.dinner},
          {breakfast: tags.breakfast}, {Desserts: tags.Dessert}, {Salad: tags.Salad}, {Soup: tags.Soup} ],
          timeToPrepare:  {[op.lte]:  timeToPrepare},
          calories: {[op.lte]:  calories}
        }
      }
      }).then(item =>{
       // console.log(item);
        res.status(200).send(item);
      })
      .catch(error => res.status(400).send(error))
    }
  }
  



