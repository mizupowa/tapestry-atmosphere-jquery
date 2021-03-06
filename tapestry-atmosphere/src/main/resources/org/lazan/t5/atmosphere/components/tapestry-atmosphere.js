var TapestryAtmosphereEvents =function () {
    var EVENT_TAPESTRY_ATMOS_PREFIX = "tapestry-atmos:";
    return {
        EVENT_TAPESTRY_ATMOS_READY : EVENT_TAPESTRY_ATMOS_PREFIX + "ready"
    };
}();

(function () {

    var atmContainer = {};


    T5.extendInitializers({
        atmosphereContainer: function (options) {
            var pushTargets = [];
            var pushTargetsById = {};
            var request = options.connectOptions;
            var subsocket;

            function updatePushTarget(targets) {
                var pushTarget;
                var i;
                for (i = 0; i < pushTargets.length; ++i) {
                    pushTarget = pushTargets[i];
                    if (typeof document.getElementById(pushTarget.id) === 'undefined' || document.getElementById(pushTarget.id) == null) {
                        pushTargets.splice(i, 1);
                        i--;
                    }
                }

                // group pushTargets by topic
                for (i = 0; i < targets.length; ++i) {
                    pushTargets.push(targets[i]);
                    pushTargetsById[targets[i].id] = targets[i];
                }
                var data = {
                    pushTargets: pushTargets,
                    ac: options.ac,
                    activePageName: options.activePageName,
                    containingPageName: options.containingPageName,
                };
                subsocket.push(JSON.stringify(data));
                for (i = 0; i < targets.length; ++i) {
                    pushTarget = targets[i];
                    if (typeof document.getElementById(pushTarget.id) !== 'undefined' || document.getElementById(pushTarget.id) != null) {
                        $(document.getElementById(pushTarget.id)).trigger(TapestryAtmosphereEvents.EVENT_TAPESTRY_ATMOS_READY)
                    }
                }
            }

            request.onOpen = function (response) {
                if (options.pushTargets && options.pushTargets.length > 0) {
                    updatePushTarget(options.pushTargets);
                }
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
                        $(element).html(content);
                    }
                    $.tapestry.utils.loadScriptsInReply(singleResponse, undefined);
                }
            };
            atmosphere.unsubscribe();
            subsocket = atmosphere.subscribe(request);

            document.getElementById(options.containerId)['atmContainer'] =
            {
                updatePushTarget: updatePushTarget
            };

        }

    });

}());

