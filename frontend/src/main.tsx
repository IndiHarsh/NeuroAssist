import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { bootstrapToken } from './lib/api'
import { LoginPage } from './pages/LoginPage'
import { RoleSelectPage } from './pages/RoleSelectPage'
import { FamilyDashboard } from './pages/FamilyDashboard'
import { PatientDashboard } from './pages/PatientDashboard'
import { QuizPage } from './pages/QuizPage'

bootstrapToken()

const router = createBrowserRouter([
  { path: '/', element: <LoginPage /> },
  { path: '/role', element: <RoleSelectPage /> },
  { path: '/family', element: <FamilyDashboard /> },
  { path: '/patient', element: <PatientDashboard /> },
  { path: '/quiz', element: <QuizPage /> },
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
