box.cfg{listen=3301}

box.schema.user.grant('guest', 'read,write,execute', 'universe')

local space = box.schema.space.create('KV', {if_not_exists=true})
space:format({
    {name='key', type='string'},
    {name='value', type='varbinary', is_nullable=true}
})
space:create_index('primary', {type='tree', parts={'key'}, if_not_exists=true})

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

function select_range(space_name, index_name, key_since, key_to, limit, offset)
    local tuples = box.space[space_name].index[index_name]:select(key_since, {iterator='GE', limit=limit, offset=offset})
    local result = {}
    for _, tuple in ipairs(tuples) do
        if tuple[1] > key_to then break end
        table.insert(result, {tuple[1], tuple[2]})
    end
    return result
end