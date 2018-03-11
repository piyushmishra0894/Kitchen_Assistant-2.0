'use strict';
module.exports = {
  up: (queryInterface, Sequelize) => {
    return queryInterface.createTable('UserDetails', {
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      Age: {
        type: Sequelize.INTEGER
      },
      Sex: {
        type: Sequelize.STRING
      },
      Nationality: {
        type: Sequelize.STRING
      },
      CurrentCity: {
        type: Sequelize.STRING
      },
      CurrentCountry: {
        type: Sequelize.STRING
      },
      createdAt: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updatedAt: {
        allowNull: false,
        type: Sequelize.DATE
      }
    });
  },
  down: (queryInterface, Sequelize) => {
    return queryInterface.dropTable('UserDetails');
  }
};