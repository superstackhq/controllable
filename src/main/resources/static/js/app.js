function displayError(container, message) {
    container.html("<div class='notification is-light is-danger' style='margin-bottom: 1rem;'>" + message + "</div>");
}

function displaySuccess(container, message) {
    container.html("<div class='notification is-light is-success' style='margin-bottom: 1rem;'>" + message + "</div>");
}

function getQueryParameter(key) {
    let params = (new URL(document.location)).searchParams;
    return params.get(key);
}

function getHeaders() {
    return {
        Authorization: "Bearer " + localStorage.getItem("token")
    }
}

function convertToDataType(value, dataType) {
    if (dataType === "INTEGER") {
        value = parseInt(value);
    }

    if (dataType === "FLOAT") {
        value = parseFloat(value);
    }

    if (dataType === "NULL") {
        value = null;
    }

    if (dataType === "BOOLEAN") {
        if (value === "true") {
            value = true;
        } else {
            value = false;
        }
    }

    if (dataType === "ARRAY" || dataType === "OBJECT") {
        value = JSON.parse(value);
    }

    return value;
}

$(document).ready(function () {
    $("body").on("click", ".js-modal-trigger", function () {
        $("#" + $(this).data("target")).addClass("is-active");
    });

    function closeModal($el) {
        $el.classList.remove('is-active');
    }

    function closeAllModals() {
        (document.querySelectorAll('.modal') || []).forEach(($modal) => {
            closeModal($modal);
        });
    }

    // Add a click event on various child elements to close the parent modal
    (document.querySelectorAll('.modal-background, .modal-close, .modal-card-head .delete, .modal-card-foot .close-button') || []).forEach(($close) => {
        const $target = $close.closest('.modal');

        $close.addEventListener('click', () => {
            closeModal($target);
        });
    });

    // Add a keyboard event to close all modals
    document.addEventListener('keydown', (event) => {
        const e = event || window.event;

        if (e.keyCode === 27) { // Escape key
            closeAllModals();
        }
    });

    $(".navbar-burger").click(function () {
        // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
        $(".navbar-burger").toggleClass("is-active");
        $(".navbar-menu").toggleClass("is-active");

    });
});

function AccountApi() {
    this.signUp = (username, password, organizationName, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/accounts/signup",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                username: username,
                password: password,
                organizationName: organizationName
            }),
            success: success,
            error: error
        });
    };

    this.authenticate = (username, password, organizationName, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/accounts/authenticate",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                username: username,
                password: password,
                organizationName: organizationName
            }),
            success: success,
            error: error
        });
    };
}

function UserApi() {

    this.getCurrentUser = (success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/users/me",
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.changePassword = (password, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/users/me/password",
            data: JSON.stringify({
                password: password
            }),
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.addUser = (username, admin, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/users",
            data: JSON.stringify({
                username: username,
                admin: admin
            }),
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getUser = (userId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/users/" + userId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteUser = (userId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/users/" + userId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.changeUserAdmin = (userId, admin, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/users/" + userId + "/admin",
            data: JSON.stringify({
                admin: admin
            }),
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.resetUserPassword = (userId, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/users/" + userId + "/password",
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listUsers = (page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/users",
            data: {
                page: page,
                size: size
            },
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.searchUsers = (query, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/users/search",
            data: {
                page: page,
                size: size,
                query: query
            },
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function OrganizationApi() {
    this.getOrganization = (success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/organization",
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.updateOrganization = (billingEmail, currency, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/organization",
            headers: getHeaders(),
            data: JSON.stringify({
                billingEmail: billingEmail,
                currency: currency
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function GroupApi() {

    this.createGroup = (name, description, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/groups",
            headers: getHeaders(),
            data: JSON.stringify({
                name: name,
                description: description
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });

    };

    this.listAllGroups = (page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/groups/all",
            headers: getHeaders(),
            data: {
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });

    };

    this.searchGroups = (query, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/groups/search",
            headers: getHeaders(),
            data: {
                query: query,
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });

    };

    this.listGroups = (page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/groups",
            headers: getHeaders(),
            data: {
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getGroup = (groupId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/groups/" + groupId,
            headers: getHeaders(),
            success: success,
            dataType: "json",
            contentType: "application/json",
            error: error
        });
    };

    this.updateGroup = (groupId, name, description, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/groups/" + groupId,
            headers: getHeaders(),
            data: JSON.stringify({
                name: name,
                description: description
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteGroup = (groupId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/groups/" + groupId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.addGroupMember = (groupId, userId, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/groups/" + groupId + "/members",
            headers: getHeaders(),
            data: JSON.stringify({
                userId: userId
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteGroupMember = (groupId, userId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/groups/" + groupId + "/members",
            headers: getHeaders(),
            data: JSON.stringify({
                userId: userId
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listGroupMembers = (groupId, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/groups/" + groupId + "/members",
            headers: getHeaders(),
            data: {
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function AccessApi() {
    this.addAccess = (targetType, targetId, environmentId, actorType, actorId, permissions, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/access",
            headers: getHeaders(),
            data: JSON.stringify({
                targetType: targetType,
                targetId: targetId,
                environmentId: environmentId,
                actorType: actorType,
                actorId: actorId,
                permissions: permissions
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteAccess = (targetType, targetId, environmentId, actorType, actorId, permissions, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/access",
            headers: getHeaders(),
            data: JSON.stringify({
                targetType: targetType,
                targetId: targetId,
                environmentId: environmentId,
                actorType: actorType,
                actorId: actorId,
                permissions: permissions
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteAllAccess = (targetType, targetId, environmentId, actorType, actorId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/access/all",
            headers: getHeaders(),
            data: JSON.stringify({
                targetType: targetType,
                targetId: targetId,
                environmentId: environmentId,
                actorType: actorType,
                actorId: actorId
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listAccesses = (targetType, targetId, environmentId, actorType, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/access",
            headers: getHeaders(),
            data: {
                targetType: targetType,
                targetId: targetId,
                environmentId: environmentId,
                actorType: actorType,
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getAccess = (targetType, targetId, environmentId, actorType, actorId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/access",
            headers: getHeaders(),
            data: {
                targetType: targetType,
                targetId: targetId,
                environmentId: environmentId,
                actorType: actorType,
                actorId: actorId
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function ApiKeyApi() {
    this.createApiKey = (name, description, hasFullAccess, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/api-keys",
            headers: getHeaders(),
            data: JSON.stringify({
                name: name,
                description: description,
                hasFullAccess: hasFullAccess
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listApiKeys = (page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/api-keys",
            headers: getHeaders(),
            data: {
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.searchApiKeys = (query, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/api-keys/search",
            headers: getHeaders(),
            data: {
                query: query,
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getApiKey = (apiKeyId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/api-keys/" + apiKeyId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.updateApiKey = (apiKeyId, name, description, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/api-keys/" + apiKeyId,
            headers: getHeaders(),
            data: JSON.stringify({
                name: name,
                description: description
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.updateApiKeyFullAccess = (apiKeyId, hasFullAccess, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/api-keys/" + apiKeyId + "/full-access",
            headers: getHeaders(),
            data: JSON.stringify({
                hasFullAccess: hasFullAccess
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getApiKeyAccessKey = (apiKeyId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/api-keys/" + apiKeyId + "/access-key",
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.resetApiKeyAccessKey = (apiKeyId, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/api-keys/" + apiKeyId + "/access-key",
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteApiKey = (apiKeyId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/api-keys/" + apiKeyId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function AppApi() {
    this.createApp = (name, description, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/apps",
            headers: getHeaders(),
            data: JSON.stringify({
                name: name,
                description: description
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listApps = (page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/apps",
            headers: getHeaders(),
            data: {
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.searchApps = (query, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/apps/search",
            headers: getHeaders(),
            data: {
                query: query,
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getApp = (appId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/apps/" + appId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.updateApp = (appId, name, description, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/apps/" + appId,
            headers: getHeaders(),
            data: JSON.stringify({
                name: name,
                description: description
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getAppAccessKey = (appId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/apps/" + appId + "/access-key",
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.resetAppAccessKey = (appId, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/apps/" + appId + "/access-key",
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteApp = (appId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/apps/" + appId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function EnvironmentApi() {
    this.createEnvironment = (name, description, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/environments",
            headers: getHeaders(),
            data: JSON.stringify({
                name: name,
                description: description
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listEnvironments = (page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/environments",
            headers: getHeaders(),
            data: {
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getEnvironment = (environmentId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/environments/" + environmentId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.updateEnvironment = (environmentId, name, description, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/environments/" + environmentId,
            headers: getHeaders(),
            data: JSON.stringify({
                name: name,
                description: description
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteEnvironment = (environmentId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/environments/" + environmentId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function CollectionApi() {
    this.createCollection = (name, description, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/collections",
            headers: getHeaders(),
            data: JSON.stringify({
                name: name,
                description: description
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });

    };

    this.listCollections = (page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/collections",
            headers: getHeaders(),
            data: {
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.searchCollections = (query, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/collections/search",
            headers: getHeaders(),
            data: {
                query: query,
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getCollection = (collectionId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/collections/" + collectionId,
            headers: getHeaders(),
            success: success,
            dataType: "json",
            contentType: "application/json",
            error: error
        });
    };

    this.updateCollection = (collectionId, name, description, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/collections/" + collectionId,
            headers: getHeaders(),
            data: JSON.stringify({
                name: name,
                description: description
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteCollection = (collectionId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/collections/" + collectionId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function PropertyApi() {
    this.createProperty = (namespace, key, version, description, dataType, segmentTreeStructure, constraints, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/properties",
            headers: getHeaders(),
            data: JSON.stringify({
                namespace: namespace,
                key: key,
                version: version,
                description: description,
                dataType: dataType,
                segmentTreeStructure: segmentTreeStructure,
                constraints: constraints
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listProperties = (page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/properties",
            headers: getHeaders(),
            data: {
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listPropertiesAtNamespace = (namespace, page, size, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/properties/fetch?page=" + page + "&size=" + size,
            headers: getHeaders(),
            data: JSON.stringify({
                namespace: namespace,
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getProperty = (propertyId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/properties/" + propertyId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.updateProperty = (propertyId, description, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/properties/" + propertyId,
            headers: getHeaders(),
            data: JSON.stringify({
                description: description
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteProperty = (propertyId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/properties/" + propertyId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function PropertyValueApi() {
    this.createPropertyValue = (propertyId, environmentId, value, segment, rule, changeMessage, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/environments/" + environmentId + "/properties/" + propertyId + "/values",
            headers: getHeaders(),
            data: JSON.stringify({
                value: value,
                segment: segment,
                rule: rule,
                changeMessage: changeMessage
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listAllPropertyValues = (environmentId, propertyId, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/environments/" + environmentId + "/properties/" + propertyId + "/values",
            headers: getHeaders(),
            data: {
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listPropertyValues = (environmentId, propertyId, segment, page, size, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/environments/" + environmentId + "/properties/" + propertyId + "/values/fetch?page=" + page + "&size=" + size,
            headers: getHeaders(),
            data: JSON.stringify({
                segment: segment
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getPropertyValue = (valueId, propertyId, environmentId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/environments/" + environmentId + "/properties/" + propertyId + "/values/" + valueId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.updatePropertyValue = (valueId, propertyId, environmentId, value, rule, changeMessage, success, error) => {
        $.ajax({
            type: "PUT",
            url: "/api/v1/environments/" + environmentId + "/properties/" + propertyId + "/values/" + valueId,
            headers: getHeaders(),
            data: JSON.stringify({
                value: value,
                rule: rule,
                changeMessage: changeMessage
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deletePropertyValue = (valueId, propertyId, environmentId, changeMessage, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/environments/" + environmentId + "/properties/" + propertyId + "/values/" + valueId,
            headers: getHeaders(),
            dataType: "json",
            data: JSON.stringify({
                changeMessage: changeMessage
            }),
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function PropertyValueLogApi() {
    this.getPropertyValueLog = (logId, valueId, propertyId, environmentId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/environments/" + environmentId + "/properties/" + propertyId + "/values/" + valueId + "/logs/" + logId,
            headers: getHeaders(),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listPropertyValueLogs = (valueId, propertyId, environmentId, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/environments/" + environmentId + "/properties/" + propertyId + "/values/" + valueId + "/logs",
            headers: getHeaders(),
            data: {
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function CollectionMemberApi() {
    this.addCollectionMember = (collectionId, affordanceType, affordanceId, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/collections/" + collectionId + "/members",
            headers: getHeaders(),
            data: JSON.stringify({
                type: affordanceType,
                referenceId: affordanceId
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteCollectionMember = (collectionId, affordanceType, affordanceId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/collections/" + collectionId + "/members",
            headers: getHeaders(),
            data: JSON.stringify({
                referenceId: affordanceId,
                type: affordanceType
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listCollectionMembers = (collectionId, affordanceType, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/collections/" + collectionId + "/members",
            headers: getHeaders(),
            data: {
                affordanceType: affordanceType,
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listAffordanceCollections = (affordanceType, affordanceId, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/affordances/collections",
            headers: getHeaders(),
            data: {
                affordanceType: affordanceType,
                affordanceId: affordanceId,
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}

function AppAccessApi() {
    this.addAppAccess = (appId, targetType, targetId, permissions, success, error) => {
        $.ajax({
            type: "POST",
            url: "/api/v1/apps/" + appId + "/access",
            headers: getHeaders(),
            data: JSON.stringify({
                targetType: targetType,
                targetId: targetId,
                permissions: permissions
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteAppAccess = (appId, targetType, targetId, permissions, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/apps/" + appId + "/access",
            headers: getHeaders(),
            data: JSON.stringify({
                targetType: targetType,
                targetId: targetId,
                permissions: permissions
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.deleteAllAppAccess = (appId, targetType, targetId, success, error) => {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/apps/" + appId + "/access/all",
            headers: getHeaders(),
            data: JSON.stringify({
                targetType: targetType,
                targetId: targetId
            }),
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.getAppAccess = (appId, targetType, targetId, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/apps/" + appId + "/access",
            headers: getHeaders(),
            data: {
                targetType: targetType,
                targetId: targetId
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    }

    this.listAppTargets = (appId, targetType, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/apps/" + appId + "/access",
            headers: getHeaders(),
            data: {
                targetType: targetType,
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };

    this.listTargetApps = (targetType, targetId, page, size, success, error) => {
        $.ajax({
            type: "GET",
            url: "/api/v1/access/apps",
            headers: getHeaders(),
            data: {
                targetType: targetType,
                targetId: targetId,
                page: page,
                size: size
            },
            dataType: "json",
            contentType: "application/json",
            success: success,
            error: error
        });
    };
}
