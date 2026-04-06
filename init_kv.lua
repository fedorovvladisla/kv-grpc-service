-- Эти функции уже должны быть, если нет — добавьте
function kv_put(key, value)
    box.space.KV:upsert({key, value}, {{'=', 2, value}})
    return true
end

function kv_get(key)
    local tuple = box.space.KV:get(key)
    return tuple and tuple[2] or nil
end

function kv_delete(key)
    box.space.KV:delete(key)
    return true
end

function kv_count()
    return box.space.KV:count()
end

-- функция select_range уже есть