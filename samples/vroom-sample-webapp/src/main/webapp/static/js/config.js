//GLOBAL CONFIG OBJECT
var CONFIG = {};

//NOTIFICATION
CONFIG.notification = {};
CONFIG.notification.duration = 3000;

//WEBSERVICE
CONFIG.webservice = {};
// the API base URL value is interpolated at build time using the value defined by the applied maven profile
// (one of vroom-sample-local-dev or vroom-sample-prod)
CONFIG.webservice.base_url = '${vroom-sample.env.api-base-url}';

//CLASSNAMES
CONFIG.webservice.className = {};
CONFIG.webservice.className.place = 'places';

//EXTERNAL FEED FILES
CONFIG.feed = {};
CONFIG.feed.state_path = '/feed/us_states.json';

