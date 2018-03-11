'use strict';

module.exports = {
  up: (queryInterface, Sequelize) => {
    return  queryInterface.addConstraint('UserDetails', ['UserID'], {
      type: 'foreign key',
      name: 'UserID_for_key',
      references: { //Required field
          table: 'Users',
          field: 'id'
  },

  onDelete: 'cascade',
  onUpdate: 'cascade'
});
  },

  down: (queryInterface, Sequelize) => {
    return queryInterface.removeConstraint('UserDetails', 'UserID_for_keys');
    /*
      Add reverting commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.dropTable('users');
    */
  }
};
