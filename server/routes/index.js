const usersController = require('../controllers').users;
const inventoriesController = require('../controllers').inventories;

module.exports = (app) => {
  app.get('/api', (req, res) => res.status(200).send({
    message: 'Welcome to the Kitchen Assistant API!',
  }));
 app.post('/api/users', usersController.create);
 app.post('/api/inventory/:id', inventoriesController.create);
 app.get('/api/inventory/:id', inventoriesController.retrieve);

};
