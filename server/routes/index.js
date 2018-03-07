const usersController = require('../controllers').users;
const inventoriesController = require('../controllers').inventories;
const ingredientsController = require('../controllers').ingredients;
const itemrecipesController = require('../controllers').itemrecipes;
const itemingredientmappingController = require('../controllers').itemingredientmapping;
const itemsController = require ('../controllers').items;

module.exports = (app) => {
  app.get('/api', (req, res) => res.status(200).send({
    message: 'Welcome to the Kitchen Assistant API!',
  }));
 app.post('/api/users', usersController.create);
 app.post('/api/inventory/:id', inventoriesController.create);
 app.get('/api/inventory/:id', inventoriesController.retrieve);

 app.put('/api/inventory/:id', inventoriesController.patch);
 app.delete('/api/inventory/:id', inventoriesController.delete); 
 app.get('/api/ingredients', ingredientsController.retrieve);
 app.get('/api/itemrecipes/:id', itemrecipesController.retrieve);
 app.get('/api/itemingredientmapping', itemingredientmappingController.retrieve);
 app.get('/api/items', itemsController.retrieve);

};
