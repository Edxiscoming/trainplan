$.fn.nodePicker = function (selectHandler) {
    var $this = $(this);
    $(this).autocomplete({
            source: function (request, response) {
                var term = request["term"].toUpperCase();
                nodeRepository.findByTerm(term, function (nodes) {
                    var data = new Array();
                    $.each(nodes, function (index, node) {
                        data.push({ id: node["id"],
                            label: node["name"], node: node});
                    });
                    response(data);
                });
            },
            select: function (event, ui) {
                selectHandler.call(this, ui.item.node);
            },
            open: function (event, ui) {
                $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
            },
            close: function (event, ui) {
                $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
            }
        }
    );
    return $this;
};

$.fn.nodePickerInitialize = function (nodeId) {
    var $this = this;
    nodeRepository.get(nodeId, function (node) {
        $this.val(node.name);
        $this.data("node", node);
    }, function (result) {
        console.log(result);
    });

    return $this;
};