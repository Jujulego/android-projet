// Erreurs
export function badrequest_error(res, msg) {
    return res.status(400).json({ msg });
}

export function notfound_error(res, msg) {
    return res.status(404).json({ msg });
}

export function internal_error(res, msg, err) {
    console.error(`${msg}: `, err);
    return res.status(500).json({ msg, err });
}

// Décorateurs
export function has_params(params, cb) {
    if (typeof params === "string") {
        params = [params];
    }

    return function(req, res, next) {
        let args = req.body;
        const missing = [];

        if (req.method === "GET") {
            args = req.query;
        }

        params.forEach((p) => {
            if (args[p] === undefined) {
                missing.push(p);
            }
        });

        if (missing.length > 0) {
            badrequest_error(res, `Missing parameter(s) : ${missing.join(', ')}`);
        } else {
            cb(req, res, next);
        }
    }
}

export function check_error_404(res, msg404, msg500, cb) {
    return function(err, obj) {
        if (err) {
            if (err.kind === "ObjectId") {
                notfound_error(res, msg404);
            } else {
                internal_error(res, msg500, err);
            }
        } else if (obj == null) {
            notfound_error(res, msg404);
        } else {
            cb(obj);
        }
    }
}

export function check_error_500(res, msg, cb) {
    return function(err, obj) {
        if (err) {
            internal_error(res, msg, err);
        } else {
            cb(obj);
        }
    }
}

// Export *
export default {
    badrequest_error, notfound_error, internal_error,
    has_params, check_error_404, check_error_500
}