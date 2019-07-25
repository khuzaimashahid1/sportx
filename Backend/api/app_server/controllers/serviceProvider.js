var serviceProvider=require('../models/serviceProvider.js');
var functions =require('../controllers/functions.js');

// Get ServiceProvider
module.exports.getServiceProvider = (callback, limit) => {
	serviceProvider.find(callback).limit(limit);
}

// Check email exists
module.exports.getServiceProviderByEmail = (email,callback) => {
	serviceProvider.findOne({email:email},callback);
}

// Get serviceProvider By ID
module.exports.getServiceProviderById = (id ,callback) =>  {
	serviceProvider.findById(id, callback);
}

// Get serviceProvider By category
module.exports.getServiceProviderByCategory = (category ,callback) =>  {
    serviceProvider.
    find({ category: {$regex: category , $options: 'i'}}).
    where('state').equals('approved').
    exec(callback);
}

// Get serviceProvider By email
module.exports.getServiceProviderByEmail = (email ,callback) =>  {
    serviceProvider.
    find({email: {$regex: email , $options: 'i'}}).
    where('state').equals('approved').
    exec(callback);
}


// Get serviceProvider By name
module.exports.getServiceProviderByName = (name ,callback) =>  {
    serviceProvider.
    find({name: {$regex: '.*' + name + '.*', $options: 'i'}}).
    where('state').equals('approved').
    exec(callback);
}

// Login
module.exports.login = (email,password,res) => {
    let record=new serviceProvider();
    serviceProvider.findOne({email:email}).
    where('state').equals('approved').
    exec(function(err,result)
        {
            if (err)
            {
                return res.status(500).json({Message:"Error in Connecting to DB",status:false});
            }

            else if(result)
            {
            		console.log(result.password);
            		if(record.comparePassword(password,result.password))
            			{
                	var result1 = result.toObject();
                	result1.status = true;
                	return res.json(result1);
            			}
            		else
            		{
                	return res.status(500).json({Message:"Wrong Email or Password",status:false});
            		}

            }

        });
}

// Get serviceProvider By address
module.exports.getServiceProviderByAddress = (keyword ,callback) =>  {
    serviceProvider.
    find({address: {$regex: '.*' + keyword + '.*', $options: 'i'}}).
    where('state').equals('approved').
    exec(callback);
}

// Get serviceProvider By location
module.exports.getServiceProviderByLocation = (lat,long,maxDistance ,callback) =>  {
    serviceProvider.
    find({
        location: {
           $nearSphere: {
              $geometry: {
                 type : "Point",
                 coordinates : [ long, lat ]
              },
              $maxDistance: maxDistance
           }
        }
      }).
    where('state').equals('approved').
    exec(callback);
}

// Add serviceProvider
module.exports.addServiceProvider = (serviceProviderform, callback) => {
    let record=new serviceProvider();
    record.name=serviceProviderform.name;
    record.contact=serviceProviderform.contact;
    record.address=serviceProviderform.address;
    record.location={long:serviceProviderform.long,
                     lat:serviceProviderform.lat};
    record.category=serviceProviderform.category;
    record.contact=serviceProviderform.contact;
    record.email=serviceProviderform.email;
    record.password=record.hashPassword(serviceProviderform.password);

    if(serviceProviderform.picture_profile)
    record.picture_profile=functions.uploadPicture(record.email+'_picture_profile',serviceProviderform.picture_profile);
    if(serviceProviderform.picture_cover)
    record.picture_cover=functions.uploadPicture(record.email+'_picture_cover',serviceProviderform.picture_cover);
    if(serviceProviderform.picture_1)
    record.picture_1=functions.uploadPicture(record.email+'_picture_1',serviceProviderform.picture_1);
    if(serviceProviderform.picture_2)
    record.picture_2=functions.uploadPicture(record.email+'_picture_2',serviceProviderform.picture_2);
    if(serviceProviderform.picture_3)
    record.picture_3=functions.uploadPicture(record.email+'_picture_3',serviceProviderform.picture_3);
    if(serviceProviderform.picture_4)
    record.picture_4=functions.uploadPicture(record.email+'_picture_4',serviceProviderform.picture_4);
    if(serviceProviderform.picture_5)
    record.picture_5=functions.uploadPicture(record.email+'_picture_5',serviceProviderform.picture_5);
    record.save(callback);
}

// Update serviceProvider
module.exports.updateServiceProvider = (email, serviceProviderform, options, callback) => {
    var query = {email: email};
    if(serviceProviderform.picture_profile)
    serviceProviderform.picture_profile=functions.uploadPicture(email+'_picture_profile',serviceProviderform.picture_profile);
    if(serviceProviderform.picture_cover)
    serviceProviderform.picture_cover=functions.uploadPicture(email+'_picture_cover',serviceProviderform.picture_cover);
    if(serviceProviderform.picture_1)
    serviceProviderform.picture_1=functions.uploadPicture(email+'_picture_1',serviceProviderform.picture_1);
    if(serviceProviderform.picture_2)
    serviceProviderform.picture_2=functions.uploadPicture(email+'_picture_2',serviceProviderform.picture_2);
    if(serviceProviderform.picture_3)
    serviceProviderform.picture_3=functions.uploadPicture(email+'_picture_3',serviceProviderform.picture_3);
    if(serviceProviderform.picture_4)
    serviceProviderform.picture_4=functions.uploadPicture(email+'_picture_4',serviceProviderform.picture_4);
    if(serviceProviderform.picture_5)
    serviceProviderform.picture_5=functions.uploadPicture(email+'_picture_5',serviceProviderform.picture_5);
    serviceProvider.findOneAndUpdate(query, { $set: serviceProviderform},options,callback);

}

// Delete serviceProvider
module.exports.removeServiceProvider = (id, callback) => {
    var query = {_id: id};
    serviceProvider.remove(query, callback);
}
