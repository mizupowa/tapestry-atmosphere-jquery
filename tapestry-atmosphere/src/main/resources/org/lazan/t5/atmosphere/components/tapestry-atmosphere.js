T5.extendInitializers({
	atmosphereContainer: function(options) {
		var pushTargets = options.pushTargets;
		var pushTargetsById = {};
		var subsocket;

		// group pushTargets by topic
		for (var i = 0; i < pushTargets.length; ++i) {
			var pushTarget = pushTargets[i];
			pushTargetsById[pushTarget.id] = pushTarget;
		}
		
		var request = options.connectOptions;

		request.onOpen = function(response) {
			var data = {
				pushTargets: pushTargets, 
				ac: options.ac,
				activePageName: options.activePageName,
				containingPageName: options.containingPageName,
			};
			subsocket.push(JSON.stringify(data));
		};		

		request.onMessage = function (response) {
			var messageJson = response.responseBody;
			// prototype specific
			var message = JSON.parse(messageJson);
			
			for (var clientId in message) {
				var singleResponse = message[clientId];
				var content = singleResponse.content;
				var pushTarget = pushTargetsById[clientId];
				
				var element = document.getElementById(clientId);
				if (pushTarget.update == 'PREPEND') {
                    $(element).prepend(content);
				} else if (pushTarget.update == 'APPEND') {
                    $(element).append(content);
				} else {
					element.html(content);
				}
                $.tapestry.utils.loadScriptsInReply(singleResponse, undefined);
			}
		};

		subsocket = atmosphere.subscribe(request);
	}
});
