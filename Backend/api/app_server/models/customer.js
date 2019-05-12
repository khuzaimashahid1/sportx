const mongoose = require('mongoose');
const bcrypt = require('bcrypt-nodejs');

// Customer Schema

const customerSchema = mongoose.Schema({
	name:{
		type: String,
	},
	email:{
		type: String,
	},
	contact:{
		type: String
	},
	picture:{
		type: String,
	},
	password:{
		type: String
	} 
});
customerSchema.methods.hashPassword = function(password){
	return bcrypt.hashSync(password,bcrypt.genSaltSync(10))
}
customerSchema.methods.comparePassword = function(password,hash){
	return bcrypt.compareSync(password,hash)
}
 

const customer = module.exports = mongoose.model('Customer', customerSchema);
