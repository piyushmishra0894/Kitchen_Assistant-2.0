const Inventory = require('../models').Inventory;

module.exports = {
  create(req, res) {
    return Inventory
      .create({
        UserId: req.body.UserId,
	      Itemid: req.body.Itemid,
	      Quantity: req.body.Quantity,
      })
      .then(inventory => res.status(201).send(inventory))
      .catch(error => res.status(400).send(error));
  },
  retrieve(req, res) {
    return Inventory.findAll({
      where: {
       UserId: req.params.id
      }})
    .then(inventory => res.status(202).send(inventory))
    .catch(error => res.status(400).send(error));
  },
  
};
