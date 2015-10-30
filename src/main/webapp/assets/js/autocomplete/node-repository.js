var nodeRepository = new Object();

nodeRepository.get = function (id, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/node-" + id,
        type: "GET",
        dataType: "json",
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

nodeRepository.findByTerm = function (term, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/?paged=true&first-result=0&max-results=20&where={alias}.name like '" +
            term + "%25' or {alias}.pinyin like '" + term +
            "%25' or {alias}.pinyin_initials like '" + term +
            "%25'&order=name-asc",
        type: "GET",
        dataType: "json",
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result.results);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

nodeRepository.new = function (successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/node-new",
        type: "GET",
        dataType: "json",
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

nodeRepository.save = function (node, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/node-save",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(node),
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

nodeRepository.update = function (node, successHandler, errorHandler) {
//    $.ajax({
//        url: "rail/resource/nodes/node-" + node.id,
//        type: "PUT",
//        dataType: "json",
//        data: JSON.stringify(node),
//        contentType: "application/json;charset=utf-8"
//    }).done(function (result) {
//        if (result.success && typeof successHandler === "function") {
//            successHandler(result.result);
//        }
//        if (!result.success && typeof errorHandler === "function") {
//            errorHandler(result.error);
//        }
//    }).fail(errorHandler);
};

nodeRepository.remove = function (id, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/node-" + id,
        type: "DELETE",
        dataType: "json",
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

var nodeViewRepository = new Object();

nodeViewRepository.getLocationRange = function (tier, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/views-location-range?where=tier ='" + tier + "'",
        type: "GET",
        dataType: "json",
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

nodeViewRepository.get = function (nodeId, id, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/node-" + nodeId + "/views/view-" + id,
        type: "GET",
        dataType: "json",
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

nodeViewRepository.findByTier = function (tier, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/views?paged=false&first-result=0&max-results=20&where=tier ='" + tier +
            "'",
//        url: "rail/resource/nodes/views?paged=false&first-result=0&max-results=20&where=tier ='" + tier +
//            "' and location_x > -4000 and location_x < 4000  and location_y > -4000 and location_y < 4000",
        type: "GET",
        dataType: "json",
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

nodeViewRepository.new = function (nodeId, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/node-" + nodeId + "/views/view-new",
        type: "GET",
        dataType: "json",
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

nodeViewRepository.save = function (nodeView, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/node-" + nodeView.parent.id + "/views/view-save",
        type: "POST",
        dataType: "json",
        data: JSON.stringify(nodeView),
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

nodeViewRepository.newAndSave = function (locationX, locationY, successHandler, errorHandler) {
    nodeRepository.new(function (node) {
        nodeRepository.save(node, function (node) {
            nodeViewRepository.new(node.id, function (nodeView) {
                nodeView.parent = node;
                nodeView.tier = rail.Network.TIER_MODE;
                nodeView.figure = "circle";
                nodeView.foreground = "orange";
                nodeView.background = "white";
                nodeView.width = 6;
                nodeView.height = 6;
                nodeView.visible = true;
                nodeView.location = {x: locationX, y: locationY}
                nodeView.noteLocation = {x: locationX, y: locationY - 20};
                nodeViewRepository.save(nodeView, successHandler, errorHandler);
            });
        });
    });
};

nodeViewRepository.update = function (nodeView, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/node-" + nodeView.parent.id + "/views/view-" + nodeView.id,
        type: "POST",
        dataType: "json",
        data: JSON.stringify(nodeView),
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};

nodeViewRepository.remove = function (nodeView, successHandler, errorHandler) {
    $.ajax({
        url: "rail/resource/nodes/node-" + nodeView.parent.id + "/views/view-" + nodeView.id,
        type: "DELETE",
        dataType: "json",
        contentType: "application/json;charset=utf-8"
    }).done(function (result) {
        if (result.success && typeof successHandler === "function") {
            successHandler(result.result);
        }
        if (!result.success && typeof errorHandler === "function") {
            errorHandler(result.error);
        }
    }).fail(errorHandler);
};
