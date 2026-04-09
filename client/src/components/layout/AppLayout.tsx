import { Outlet } from "react-router-dom";
import CommonHeader from "./CommonHeader";

export default function AppLayout() {
    return (
        <>
            <CommonHeader />
            <main className="app-main">
                <Outlet />
            </main>
        </>
    );
}