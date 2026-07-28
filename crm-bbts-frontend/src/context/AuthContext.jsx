import { createContext, useContext, useState, useCallback } from 'react'
import { authService } from '../api/authService'

const AuthCtx = createContext(null)

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(() => authService.usuarioAtual())

  const login = useCallback(async (username, senha) => {
    const data = await authService.login(username, senha)
    setUsuario(authService.usuarioAtual())
    return data
  }, [])

  const logout = useCallback(() => {
    authService.logout()
    setUsuario(null)
  }, [])

  return (
    <AuthCtx.Provider value={{ usuario, login, logout, isVendedor: usuario?.perfil === 'VENDEDOR', isGerente: usuario?.perfil === 'GERENTE' || usuario?.perfil === 'ADMIN' }}>
      {children}
    </AuthCtx.Provider>
  )
}

export const useAuth = () => useContext(AuthCtx)
