
box.cfg{
    listen = 3301,
}

-- Создаём пользователя для приложения (если не существует)
-- Пароль и имя должны совпадать с теми, что в Java-конфигурации
box.schema.user.create('storage', { password = 'secret-cluster-cookie' }, { if_not_exists = true })
box.schema.user.grant('storage', 'read,write,execute', 'universe')

-- Создаём спейс KV с нужной схемой
local kv = box.schema.space.create('KV', { if_not_exists = true })
kv:format({
    { name = 'key', type = 'string' },
    { name = 'value', type = 'varbinary', is_nullable = true }
})
kv:create_index('primary', {
    type = 'tree',
    parts = { 'key' },
    unique = true,
    if_not_exists = true
})