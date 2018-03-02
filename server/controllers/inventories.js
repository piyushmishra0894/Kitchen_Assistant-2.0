const Inventory = require('../models').Inventory
const Ingredient = require('../models').Ingredient
const Sequelize = require('sequelize')
const Promise = require('bluebird')
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
  //Not tested -- need to recheck the validity os this code.
  patch(req,res){
    return Inventory
    .update({
      Quantity: req.params.Quantity,
      where: {
        UserId: req.params.id
      }
    })
  },
  //Tested with client integeration with display of Ingredient Quantity and Ingredient name.
  retrieve(req, res) { 
    return Inventory.findAll({
      include: [Ingredient],
      where: {
       UserId: req.params.id
      }})
    .then(inventory => res.status(200).send(inventory))
    .catch(error => res.status(400).send(error))
    

    /*return Inventory.findAll({
      where: {
        UserId: req.params.id
      }
    // include: [Ingredient]
    })
      .then(inventory => {
        promises = [];
          promises.push(new Promise(function(resolve,reject){
            let resObj = inventory.map(value => {
                var temp = [];
                  return Ingredient.findOne({
                    where: {id: value.Itemid
                  }})
                  .then(item => {
                    const tobj = Object.assign(
                      {},
                      {
                        id: item.id,
                        item: value,
                        name: item.name
                      }
                    );
                    return tobj;
                  }).then(tempObj => {return tempObj});
            });
            Promise.all(resObj).then(results => {
              console.log("resObj" + results);
              resolve(results);
            })
          }))  
        
           /*
          arry = [];
          promises = [];
          inventory.forEach(function (value, index) {
          promises.push(new Promise(function(resolve,reject){
           Ingredient.findOne({
              where: {id: value.Itemid
            }})
              .then(item => {
                const resObj = inventory.map(value => {
                  return Object.assign(
                    {},
                    {
                      id: item.id,
                      UserId: inventory[0].UserId,
                      name: item.name
                    }
                  )
                });
                arry.push(resObj);
                console.log(resObj);
                resolve(arry);
              })
              .catch(error => {
                console.log(error);
                res.status(401).send(error);})
          }))
        });

        Promise.all(promises).then(function(data) {
          //console.log("result:" + data);
          res.status(201).send(data)})
        .catch(error => res.status(401).send(error));
      })
      .catch(error => {
        console.log(error);
        res.status(400).send(error);
      });*/
  }

}